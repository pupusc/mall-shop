package com.wanmi.sbc.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.pay.api.request.AliPayRefundRequest;
import com.wanmi.sbc.pay.api.request.PayExtraRequest;
import com.wanmi.sbc.pay.api.request.PayRequest;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.bean.enums.TradeType;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGateway;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import com.wanmi.sbc.pay.repository.ChannelItemRepository;
import com.wanmi.sbc.pay.repository.GatewayRepository;
import com.wanmi.sbc.pay.repository.TradeRecordRepository;
import com.wanmi.sbc.pay.utils.GeneratorUtils;
import com.wanmi.sbc.pay.utils.PayValidates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @program: service-pay
 * @description: 支付宝
 * @create: 2019-01-28 16:37
 **/
@Service
public class AlipayService {

    @Resource
    private TradeRecordRepository recordRepository;
    @Resource
    private ChannelItemRepository channelItemRepository;

    @Resource
    private GatewayRepository gatewayRepository;

//    String payDev = "https://openapi.alipaydev.com/gateway.do";
    //支付宝网关
    private static final String PAY = "https://openapi.alipay.com/gateway.do";

    //回调地址
    private static final String CALLBACK = "/tradeCallback/aliPayCallBack";

    /*
     * @Description: 支付宝支付接口。调用sdk生成支付数据
     * @Author: Bob
     * @Date: 2019-02-26 17:11
    */
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    public String pay(@Valid PayExtraRequest request) {
        PayChannelItem item = getPayChannelItem(request.getChannelItemId(),request.getStoreId());
        //该付款方式是否支持该渠道
        if (item.getTerminal() != request.getTerminal()) {
            throw new SbcRuntimeException("K-100202");
        }

        PayValidates.verifyGateway(item.getGateway());

        //订单重复付款检验
        PayTradeRecord record = recordRepository.findByBusinessId(request.getBusinessId());
        if (!Objects.isNull(record)) {
            //如果重复支付，判断状态，已成功状态则做异常提示
            if (record.getStatus() == TradeStatus.SUCCEED)
                throw new SbcRuntimeException("K-100203");
        }

        //支付宝的参数
        String appId = item.getGateway().getConfig().getAppId();
        String appPrivateKey = item.getGateway().getConfig().getPrivateKey();
        String aliPayPublicKey = item.getGateway().getConfig().getPublicKey();
        String notifyUrl = item.getGateway().getConfig().getBossBackUrl();
        String id = item.getId() + "";

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(PAY, appId, appPrivateKey, "json", "UTF-8",
                aliPayPublicKey, "RSA2");

        String form = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", request.getBusinessId());
        jsonObject.put("total_amount", request.getAmount());
        jsonObject.put("subject", request.getSubject());
        jsonObject.put("body", request.getBody());
        //不同的下单渠道走不同的支付接口
        if (TerminalType.PC.equals(request.getTerminal())) {
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(request.getSuccessUrl());
            alipayRequest.setNotifyUrl(getNotifyUrl(item.getGateway().getConfig()));
            jsonObject.put("product_code", "FAST_INSTANT_TRADE_PAY");
            jsonObject.put("passback_params", id);
//        jsonObject.put("timeout_express","1m");
            alipayRequest.setBizContent(jsonObject.toString());


            try {
                form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
                throw new SbcRuntimeException("K-100208",new Object[]{"PC支付"});
            }
        } else if (TerminalType.H5.equals(request.getTerminal())) {
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
            alipayRequest.setReturnUrl(request.getSuccessUrl());
            alipayRequest.setNotifyUrl(getNotifyUrl(item.getGateway().getConfig()));
            jsonObject.put("product_code", "QUICK_WAP_PAY");
            jsonObject.put("passback_params", id);
//        jsonObject.put("timeout_express","1m");
            alipayRequest.setBizContent(jsonObject.toString());
            try {
                form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
                throw new SbcRuntimeException("K-100208",new Object[]{"H5支付"});
            }
        } else if (TerminalType.APP.equals(request.getTerminal())){
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody(request.getBody());
            model.setSubject(request.getSubject());
            model.setOutTradeNo(request.getBusinessId());
//            model.setTimeoutExpress("30m");
            model.setTotalAmount(request.getAmount().toString());
            model.setProductCode("QUICK_MSECURITY_PAY");
            model.setPassbackParams(id);
            alipayRequest.setBizModel(model);
            alipayRequest.setNotifyUrl(getNotifyUrl(item.getGateway().getConfig()));
            try {
                //这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(alipayRequest);
                form = response.getBody();
            } catch (AlipayApiException e) {
                e.printStackTrace();
                throw new SbcRuntimeException("K-100208",new Object[]{"APP支付"});
            }
        }
        savePayRecord(request);
        return form;
    }

    /*
     * @Description: 新增、更新交易记录
     * @Author: Bob
     * @Date: 2019-03-05 13:54
    */
    private void savePayRecord(PayRequest request) {
        PayTradeRecord record = recordRepository.findByBusinessId(request.getBusinessId());
        if(record == null) {
            record = new PayTradeRecord();
            record.setId(GeneratorUtils.generatePT());
            record.setCreateTime(LocalDateTime.now());
        }
        record.setApplyPrice(request.getAmount());
        record.setBusinessId(request.getBusinessId());
        record.setChargeId(request.getBusinessId());
        record.setClientIp(request.getClientIp());
        record.setChannelItemId(request.getChannelItemId());
//        record.setTradeNo(result.getTradeNo());
        record.setTradeType(TradeType.PAY);
        record.setStatus(TradeStatus.PROCESSING);
//        recordRepository.deleteByBusinessId(request.getBusinessId());
        recordRepository.save(record);
    }

    /*
     * @Description: 支付宝退款接口
     * @Author: Bob
     * @Date: 2019-02-26 17:11
    */
    public AlipayTradeRefundResponse tradeRefund(AliPayRefundRequest refundRequest) {

        AlipayClient alipayClient = new DefaultAlipayClient(PAY, refundRequest.getAppid(),
                refundRequest.getAppPrivateKey(),
                "json","UTF-8",refundRequest.getAliPayPublicKey(), "RSA2");

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", refundRequest.getBusinessId());   //订单支付时传入的商户订单号,不能和 trade_no同时为空。
        jsonObject.put("refund_amount", refundRequest.getAmount());  //需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
        jsonObject.put("refund_reason",refundRequest.getDescription());  //退款的原因说明
        jsonObject.put("out_request_no",refundRequest.getRefundBusinessId()); //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        request.setBizContent(jsonObject.toString());

        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new SbcRuntimeException("K-100208",new Object[]{"退款接口"});
        }
        return response;
    }

    /*
     * @Description: 获取配置信息
     * @Author: Bob
     * @Date: 2019-03-05 13:55
    */
    private PayChannelItem getPayChannelItem(Long channelItemId,Long storeId) {
        PayChannelItem item = channelItemRepository.findById(channelItemId).get();
        PayValidates.verfiyPayChannelItem(item);
        // 获取网关
        PayGateway gateway = gatewayRepository.queryByNameAndStoreId(item.getGatewayName(),storeId);
        item.setGateway(gateway);
        return item;
    }

    /*
     * @Description: 订单ID查询支付宝平台端的此单的付款状态
     * @Param:  businessId 订单ID
     * @Author: Bob
     * @Date: 2019-03-05 13:57
    */
    private AlipayTradeQueryResponse queryAlipayPaymentSlip(AlipayClient alipayClient, String businessId){
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", businessId);
        request.setBizContent(jsonObject.toString());
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new SbcRuntimeException("K-100208",new Object[]{"查询订单接口"});
        }
        return response;
    }

    private  String getNotifyUrl(PayGatewayConfig payGatewayConfig){
        StringBuilder notify_url = new StringBuilder();
        notify_url.append(payGatewayConfig.getBossBackUrl()+CALLBACK);
        notify_url.append("/");
        notify_url.append(payGatewayConfig.getStoreId());
        return notify_url.toString();

    }
}
