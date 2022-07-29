package com.wanmi.sbc.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.pingplusplus.model.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.constant.ErrorCodeConstant;
import com.wanmi.sbc.common.constant.PaidCardConstant;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.provider.paidcard.PaidCardSaveProvider;
import com.wanmi.sbc.customer.api.provider.paidcardbuyrecord.PaidCardBuyRecordQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordByIdRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordByIdResponse;
import com.wanmi.sbc.customer.bean.dto.PaidCardRedisDTO;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsProvider;
import com.wanmi.sbc.order.api.provider.paycallbackresult.PayCallBackResultProvider;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultAddRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderCodeRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByConditionRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByIdRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderOnlineRefundRequest;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnCodeResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetPayOrderByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeListByParentIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradePageCriteriaResponse;
import com.wanmi.sbc.order.bean.dto.*;
import com.wanmi.sbc.order.bean.enums.*;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.api.provider.AliPayProvider;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.*;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.bean.enums.TradeType;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayConstants;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayUtil;
import com.wanmi.sbc.util.DateUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

/**
 * 交易回调
 * Created by sunkun on 2017/8/8.
 */
@Api(tags = "PayCallbackController", description = "交易回调")
@RestController
@RequestMapping("/tradeCallback")
@Slf4j
public class PayCallbackController {

    public static final Logger LOGGER = LoggerFactory.getLogger(PayCallbackController.class);

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private RefundOrderQueryProvider refundOrderQueryProvider;

    @Autowired
    private AliPayProvider aliPayProvider;

    @Autowired
    private WxPayProvider wxPayProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private PayCallBackResultProvider payCallBackResultProvider;

    @Autowired
    private PayCallBackTaskService payCallBackTaskService;

    @Autowired
    private ProviderTradeQueryProvider providerTradeQueryProvider;

    @Autowired
    private ProviderTradeProvider providerTradeProvider;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PaidCardSaveProvider paidCardSaveProvider;

    @Autowired
    private PaidCardBuyRecordQueryProvider paidCardBuyRecordQueryProvider;





    /**
     * 获取所有回调的参数
     * @param request
     * @return 参数Map
     */
    private static Map<String, String> getAllRequestParam(
            final HttpServletRequest request) {
        Map<String, String> res = new HashMap<>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (res.get(en) == null || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }

//    @ApiOperation(value = "ping++回调方法")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "body", dataType = "Map",
//                    name = "json", value = "回调报文", required = true),
//            @ApiImplicitParam(paramType = "header", dataType = "String",
//                    name = "x-pingplusplus-signature", value = "签名", required = true)
//    })
    @RequestMapping(value = "/pingCallBack", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity pingCallBack(@RequestBody Map<String, Object> json,
                                       @RequestHeader("x-pingplusplus-signature") String signatureStr) {
        boolean flag = true;
        String body = null;
        Event event;
        //渠道方交易回调请求参数
        TradeCallbackRequest tradeCallbackRequest = new TradeCallbackRequest();
        String objectId = null;
        try {
            body = JSON.toJSONString(json, SerializerFeature.WriteMapNullValue);
            event = Webhooks.eventParse(body);
            /*
             * 签名校验
             */
            PayGatewayConfigResponse payGatewayConfig = payQueryProvider.getGatewayConfigByGateway(new
                    GatewayConfigByGatewayRequest(PayGatewayEnum.PING, Constants.BOSS_DEFAULT_STORE_ID)).getContext();
            byte[] signatureBytes = Base64.decodeBase64(signatureStr);
            Signature signature = Signature.getInstance("SHA256withRSA");
            PublicKey publicKey = getPubKey(payGatewayConfig.getPublicKey());
            signature.initVerify(publicKey);
            signature.update(body.getBytes("UTF-8"));
            boolean isCheck = signature.verify(signatureBytes);
            System.err.println(isCheck);
            if (!isCheck) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            PingppObject obj = Webhooks.getObject(body);
            PayTradeRecordResponse payTradeRecord;
            Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name("PING")
                    .account("PING").platform(Platform.THIRD).build();
            System.err.println("-----------------------------0----------------------------------");
            System.err.println(event.getType());
            System.err.println("-----------------------------0----------------------------------");
            switch (event.getType()) {
                //支付回调
                case "charge.succeeded":
                    Charge charge = (Charge) obj;
                    objectId = charge.getId();
                    //查询交易记录
                    payTradeRecord = payQueryProvider.getTradeRecordByChargeId(new TradeRecordByChargeRequest
                            (objectId)).getContext();
                    TradeGetByIdResponse trade =
                            tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(payTradeRecord.getBusinessId()).build()).getContext();

                    TradeGetPayOrderByIdResponse tradeGetPayOrderByIdResponse =
                            tradeQueryProvider.getPayOrderById(TradeGetPayOrderByIdRequest.builder().
                                    payOrderId(trade.getTradeVO().getPayOrderId()).build()).getContext();


                    TradePayCallBackOnlineRequest tradePayCallBackOnlineRequest =

                            TradePayCallBackOnlineRequest.builder()
                                    .trade(KsBeanUtil.convert(trade.getTradeVO(), TradeDTO.class))
                                    .payOrderOld(KsBeanUtil.convert(tradeGetPayOrderByIdResponse.getPayOrder(),
                                            PayOrderDTO.class))
                                    .operator(operator)
                                    .build();

                    tradeProvider.payCallBackOnline(tradePayCallBackOnlineRequest);

//                    tradeService.payCallBackOnline(trade, tradeGetPayOrderByIdResponse.getPayOrder(), operator);
                    tradeCallbackRequest.setAmount(new BigDecimal(charge.getAmount() / 100));
                    tradeCallbackRequest.setFinishTime(DateUtil.getLocalDateTimeFromUnixTimestamp(charge.getTimePaid()));
                    tradeCallbackRequest.setTradeType(TradeType.PAY);

                    // 订单支付回调同步供应商订单状态
                    //this.providerTradePayCallBack(Collections.singletonList(trade.getTradeVO()));
                    break;
                case "refund.succeeded":
                    //退款回调
                    Refund refund = (Refund) obj;
                    objectId = refund.getId();
                    //查询交易记录
                    payTradeRecord = payQueryProvider.getTradeRecordByChargeId(new TradeRecordByChargeRequest
                            (objectId)).getContext();
                    ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder()
                            .rid(payTradeRecord.getBusinessId()).build()).getContext();
                    RefundOrderByReturnCodeResponse refundOrder =
                            refundOrderQueryProvider.getByReturnOrderCode(new RefundOrderByReturnOrderCodeRequest(payTradeRecord.getBusinessId())).getContext();
                    returnOrderProvider.onlineRefund(
                            ReturnOrderOnlineRefundRequest.builder().operator(operator)
                                    .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
                                    .refundOrder(KsBeanUtil.convert(refundOrder, RefundOrderDTO.class)).build());
                    tradeCallbackRequest.setAmount(new BigDecimal(refund.getAmount() / 100));
                    tradeCallbackRequest.setFinishTime(DateUtil.getLocalDateTimeFromUnixTimestamp(refund.getTimeSucceed
                            ()));
                    tradeCallbackRequest.setTradeType(TradeType.REFUND);
                    break;
            }
        } catch (Exception e) {
            flag = false;
            LOGGER.error("Callback business handles exceptions,json:{},signature={},", new Object[]{body,
                    signatureStr}, e);
        }
        //业务状态修改成功，修改支付记录状态
        tradeCallbackRequest.setObjectId(objectId);
        tradeCallbackRequest.setTradeStatus(TradeStatus.SUCCEED);
        tradeCallbackRequest.setTradeCount(1);
        try {
            System.err.println("------------------------------1--------------------------------------------");
            System.err.println(JSON.toJSONString(tradeCallbackRequest));
            System.err.println("-------------------------------1-------------------------------------------");
            payProvider.callback(tradeCallbackRequest);
        } catch (Exception e) {
            LOGGER.error("Execute System method [PayService.callBack] exceptions,request={},", tradeCallbackRequest, e);
        }
        return flag ? ResponseEntity.ok().build() : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 银联企业支付异步回调
     * @param request
     * @param response
     */
    @ApiOperation(value = "银联企业支付异步回调")
    @RequestMapping(value = "/unionB2BCallBack", method = RequestMethod.POST)
    @GlobalTransactional
    public void unionB2BCallBack(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        log.info("=========================银联企业支付异步回调接收报文返回开始=======================");
        String encoding = request.getParameter("encoding");
        log.info("返回报文中encoding=[" + encoding + "]");
        Map<String, String> respParam = getAllRequestParam(request);
        //验签
        if (!payProvider.unionCheckSign(respParam).getContext()) {
            log.info("验证签名结果[失败].");
        } else {
            log.info("验证签名结果[成功].");
            log.info("-------------银联支付回调,respParam：{}------------", JSONObject.toJSONString(respParam));
            String respCode = respParam.get("respCode");
            if ("00".equals(respCode)) {
                //判断respCode=00 后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
                UnionPayRequest unionPayRequest = new UnionPayRequest();
                unionPayRequest.setApiKey(request.getParameter("merId"));
                unionPayRequest.setBusinessId(request.getParameter("orderId"));
                unionPayRequest.setTxnTime(request.getParameter("txnTime"));
                Map<String, String> resultMap = payQueryProvider.getUnionPayResult(unionPayRequest).getContext();
                payProvider.unionCallBack(resultMap);

                PayTradeRecordResponse payTradeRecord;
                //交易成功，更新商户订单状态
                if (resultMap != null && "00".equals(resultMap.get("respCode"))) {
                    payTradeRecord = payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest
                            (resultMap.get("orderId"))).getContext();
                    Operator operator =
                            Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name(PayGatewayEnum.UNIONB2B.name())
                                    .account(PayGatewayEnum.UNIONB2B.name()).platform(Platform.THIRD).build();

                    //单笔支付与组合支付区分，获取支付订单的信息
                    boolean isMergePay = isMergePayOrder(payTradeRecord.getBusinessId());
                    List<TradeVO> trades = new ArrayList<>();
                    if (!isMergePay) {
                        if (isTailPayOrder(payTradeRecord.getBusinessId())) {
                            trades.add(tradeQueryProvider.listAll(TradeListAllRequest.builder().tradeQueryDTO(TradeQueryDTO.builder().tailOrderNo(payTradeRecord.getBusinessId()).build()).build()).getContext().getTradeVOList().get(0));
                        } else {
                            trades.add(tradeQueryProvider.getById(TradeGetByIdRequest
                                    .builder().tid(payTradeRecord.getBusinessId()).build()).getContext().getTradeVO());
                        }
                    } else {
                        trades.addAll(tradeQueryProvider.getListByParentId(TradeListByParentIdRequest.builder()
                                .parentTid(payTradeRecord.getBusinessId()).build()).getContext().getTradeVOList());
                    }
                    payCallbackOnline(trades, operator, isMergePay);
                }
                response.getWriter().print("ok");
            }

        }

        log.info("银联企业支付异步回调接收报文返回结束");

    }

    /**
     * 银联退款回调
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @ApiOperation(value = "银联退款回调")
    @RequestMapping(value = "/unionRefundCallBack", method = RequestMethod.POST)
    @GlobalTransactional
    public void unionRefundCallBack(HttpServletRequest request, HttpServletResponse response) throws ServletException
            , IOException {
        log.info("退款回调接收后台通知开始");
        //String encoding = "UTF-8";
        // 获取银联通知服务器发送的后台通知参数
        Map<String, String> reqParam = getAllRequestParam(request);
        //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!payProvider.unionCheckSign(reqParam).getContext()) {
            log.info("验证签名结果[失败].");
            //验签失败，需解决验签问题

        } else {
            log.info("验证签名结果[成功].");
            //【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
            //判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
            String respCode = reqParam.get("respCode");
            if ("00".equals(respCode)) {
                PayTradeRecordResponse payTradeRecord =
                        payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest
                                (reqParam.get("orderId"))).getContext();
                ReturnOrderVO returnOrder;
                if (payTradeRecord.getBusinessId().startsWith("RT")) {
                    returnOrder = returnOrderQueryProvider.listByCondition(ReturnOrderByConditionRequest.builder()
                            .businessTailId(payTradeRecord.getBusinessId()).build()).getContext().getReturnOrderList().get(0);
                } else {
                    returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder()
                            .rid(payTradeRecord.getBusinessId()).build()).getContext();
                }
                RefundOrderByReturnCodeResponse refundOrder =
                        refundOrderQueryProvider.getByReturnOrderCode(new RefundOrderByReturnOrderCodeRequest(payTradeRecord.getBusinessId())).getContext();
                if (payTradeRecord.getBusinessId().startsWith("RT")) {
                    refundOrder.setRefundChannel(RefundChannel.TAIL);
                }
                Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name("UNIONB2B")
                        .account("UNIONB2B").platform(Platform.THIRD).build();
                payProvider.unionCallBack(reqParam);
                returnOrderProvider.onlineRefund(
                        ReturnOrderOnlineRefundRequest.builder().operator(operator)
                                .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
                                .refundOrder(KsBeanUtil.convert(refundOrder, RefundOrderDTO.class)).build());
                response.getWriter().print("ok");
            }
        }
        log.info("退款回调接收后台通知结束");
    }

//    /**
//     * 微信支付异步回调
//     * @param request
//     * @param response
//     */
//    @ApiOperation(value = "微信支付异步回调")
//    @RequestMapping(value = "/WXPaySuccessCallBack1/{storeId}", method = {RequestMethod.POST, RequestMethod.GET})
//    @GlobalTransactional
//    public void paySuc(HttpServletRequest request, HttpServletResponse response ,@PathVariable("storeId") Long storeId) throws Exception {
//        log.info("======================微信支付异步通知回调start======================");
//
//        PayGatewayConfigResponse payGatewayConfig = payQueryProvider.getGatewayConfigByGateway(new
//                GatewayConfigByGatewayRequest(PayGatewayEnum.WECHAT, storeId)).getContext();
//        String apiKey = payGatewayConfig.getApiKey();
//        //支付回调结果
//        String result = WXPayConstants.SUCCESS;
//        //微信回调结果参数对象
//        WxPayResultResponse wxPayResultResponse = new WxPayResultResponse();
//        try {
//            //获取回调数据输入流
//            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//            String line;
//            StringBuilder retXml = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                retXml.append(line);
//            }
//            //微信支付异步回调结果xml
//            log.info("异步通知回调结果微信支付xml====" + retXml);
//            String retXmlStr = retXml.toString();
//            retXmlStr = retXmlStr.replaceAll("<coupon_id_[0-9]{0,11}[^>]*>(.*?)</coupon_id_[0-9]{0,11}>", "");
//            retXmlStr = retXmlStr.replaceAll("<coupon_type_[0-9]{0,11}[^>]*>(.*?)</coupon_type_[0-9]{0,11}>", "");
//            retXmlStr = retXmlStr.replaceAll("<coupon_fee_[0-9]{0,11}[^>]*>(.*?)</coupon_fee_[0-9]{0,11}>", "");
//
//            XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
//
//            xStream.alias("xml", WxPayResultResponse.class);
//            wxPayResultResponse = (WxPayResultResponse) xStream.fromXML(retXmlStr);
//            log.info("-------------微信支付回调,wxPayResultResponse：{}------------", wxPayResultResponse);
//            //判断当前回调是否是合并支付
//            String businessId = wxPayResultResponse.getOut_trade_no();
//            boolean isMergePay = isMergePayOrder(businessId);
//            String lockName;
//            //非组合支付，则查出该单笔订单。
//            if (!isMergePay) {
//                TradeVO tradeVO;
//                if (isTailPayOrder(businessId)) {
//                    tradeVO = tradeQueryProvider.listAll(TradeListAllRequest.builder().tradeQueryDTO(TradeQueryDTO.builder().tailOrderNo(businessId).build()).build()).getContext().getTradeVOList().get(0);
//                } else {
//                    tradeVO = tradeQueryProvider.getById(new TradeGetByIdRequest(businessId)).getContext().getTradeVO();
//                }
//                // 锁资源：无论是否组合支付，都锁父单号，确保串行回调
//                lockName = tradeVO.getParentId();
//            } else {
//                lockName = businessId;
//            }
//            //redis锁，防止同一订单重复回调
//            RLock rLock = redissonClient.getFairLock(lockName);
//            rLock.lock();
//            //执行回调
//            try {
//                //支付回调事件成功
//                if (wxPayResultResponse.getReturn_code().equals(WXPayConstants.SUCCESS) &&
//                        wxPayResultResponse.getResult_code().equals(WXPayConstants.SUCCESS)) {
//                    log.info("微信支付异步通知回调状态---成功");
//                    //微信回调参数数据map
//                    Map<String, String> params = WXPayUtil.xmlToMap(retXml.toString());
//                    String trade_type = wxPayResultResponse.getTrade_type();
//                    //app支付回调对应的api key为开放平台对应的api key
//                    if (trade_type.equals("APP")) {
//                        apiKey = payGatewayConfig.getOpenPlatformApiKey();
//                    }
//                    //微信签名校验
//                    if (WXPayUtil.isSignatureValid(params, apiKey)) {
//                        //签名正确，进行逻辑处理--对订单支付单以及操作信息进行处理并添加交易数据
//                        List<TradeVO> trades = new ArrayList<>();
//                        //查询交易记录
//                        TradeRecordByOrderCodeRequest tradeRecordByOrderCodeRequest =
//                                new TradeRecordByOrderCodeRequest(businessId);
//                        PayTradeRecordResponse recordResponse =
//                                payQueryProvider.getTradeRecordByOrderCode(tradeRecordByOrderCodeRequest).getContext();
//                        if (isMergePay) {
//                            /*
//                             * 合并支付
//                             * 查询订单是否已支付或过期作废
//                             */
//                            trades = tradeQueryProvider.getOrderListByParentId(
//                                    new TradeListByParentIdRequest(businessId)).getContext().getTradeVOList();
//                            //订单合并支付场景状态采样
//                            boolean paid =
//                                    trades.stream().anyMatch(i -> i.getTradeState().getPayState() == PayState.PAID);
//
//                            boolean cancel =
//                                    trades.stream().anyMatch(i -> i.getTradeState().getFlowState() == FlowState.VOID);
//
//
//                            if (cancel || (paid && !recordResponse.getTradeNo().equals(wxPayResultResponse.getTransaction_id()))) {
//                                //同一批订单重复支付或过期作废，直接退款
//                                wxRefundHandle(wxPayResultResponse, businessId, storeId);
//                            } else {
//                                wxPayCallbackHandle(payGatewayConfig, wxPayResultResponse, businessId, trades, true);
//                            }
//
//                        } else {
//                            //单笔支付
//                            TradeVO tradeVO;
//                            if (isTailPayOrder(businessId)) {
//                                tradeVO = tradeQueryProvider.listAll(TradeListAllRequest.builder().tradeQueryDTO(TradeQueryDTO.builder().tailOrderNo(businessId).build()).build()).getContext().getTradeVOList().get(0);
//                            } else {
//                                tradeVO = tradeQueryProvider.getOrderById(new TradeGetByIdRequest(businessId))
//                                        .getContext().getTradeVO();
//                            }
//                            if (tradeVO.getTradeState().getFlowState() == FlowState.VOID || (tradeVO.getTradeState()
//                                    .getPayState() == PayState.PAID
//                                    && !recordResponse.getTradeNo().equals(wxPayResultResponse.getTransaction_id()))) {
//                                //同一批订单重复支付或过期作废，直接退款
//                                wxRefundHandle(wxPayResultResponse, businessId, storeId);
//                            } else {
//                                trades.add(tradeVO);
//                                wxPayCallbackHandle(payGatewayConfig, wxPayResultResponse, businessId, trades, false);
//                            }
//                        }
//                    } else {
//                        log.info("微信支付异步回调验证签名结果[失败].");
//                        result = "fail";
//                    }
//                } else {
//                    log.info("微信支付异步通知回调状态---失败");
//                    result = "fail";
//                }
//                log.info("微信支付异步通知回调end---------");
//            } finally {
//                //解锁
//                rLock.unlock();
//            }
//        } catch (Exception e) {
//            log.error("微信异步通知处理失败:", e);
//            result = "fail";
//            throw e;
//        } finally {
//            // 异步消息接收后处理结果返回微信
//            wxCallbackResultHandle(response, result);
//            log.info("微信异步通知完成：结果=" + result + "，微信交易号=" + wxPayResultResponse.getTransaction_id());
//        }
//    }

    /**
     * 微信支付异步回调
     * @param request
     * @param response
     */
    @ApiOperation(value = "微信支付异步回调")
    @RequestMapping(value = "/WXPaySuccessCallBack/{storeId}", method = {RequestMethod.POST, RequestMethod.GET})
    @GlobalTransactional
    public void wxPayBack(HttpServletRequest request, HttpServletResponse response ,@PathVariable("storeId") Long storeId) throws Exception {
        log.info("支付成功回调 微信支付异步通知 begin");
        //支付回调结果
        String result = WXPayConstants.SUCCESS;
        //微信回调结果参数对象
        WxPayResultResponse wxPayResultResponse = new WxPayResultResponse();
        try {
            //获取回调数据输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder retXml = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                retXml.append(line);
            }
            //微信支付异步回调结果xml
            log.info("支付回调 微信支付异步通知回调结果 {} ", retXml);
            String retXmlStr = retXml.toString();
//            String retXmlStr = "<xml><appid><![CDATA[wxb67fac0bbb6b4031]]></appid><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1489104242]]></mch_id><nonce_str><![CDATA[jr4itIa2Kin4j9p5VRV1UDAmZLsZGA8P]]></nonce_str><openid><![CDATA[o6wq55YZ9xrgyQwQeka6btYt5HOQ]]></openid><out_trade_no><![CDATA[O202007011444302542]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[DA4F5784AEA7A17B608267EF8131A87A]]></sign><time_end><![CDATA[20200701144442]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000616202007015217733087]]></transaction_id></xml>";
            retXmlStr = retXmlStr.replaceAll("<coupon_id_[0-9]{0,11}[^>]*>(.*?)</coupon_id_[0-9]{0,11}>", "");
            retXmlStr = retXmlStr.replaceAll("<coupon_type_[0-9]{0,11}[^>]*>(.*?)</coupon_type_[0-9]{0,11}>", "");
            retXmlStr = retXmlStr.replaceAll("<coupon_fee_[0-9]{0,11}[^>]*>(.*?)</coupon_fee_[0-9]{0,11}>", "");

            //将回调数据写入数据库
            XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
            xStream.alias("xml", WxPayResultResponse.class);
            wxPayResultResponse = (WxPayResultResponse) xStream.fromXML(retXmlStr);
            log.info("支付回调 微信支付异步通知回调结果解析信息：{}", JSON.toJSONString(wxPayResultResponse));
            //如果线程池队列已满，则采取拒绝策略（AbortPolicy），抛出RejectedExecutionException异常，则将对应的回调改为处理失败，然后通过定时任务处理补偿
            try {
                addPayCallBackResult(PayCallBackResultAddRequest.builder()
                        .businessId(wxPayResultResponse.getOut_trade_no())
                        .resultXml(retXml.toString())
                        .resultContext(retXmlStr)
                        .resultStatus(PayCallBackResultStatus.HANDLING)
                        .errorNum(0)
                        .payType(PayCallBackType.WECAHT)
                        .build());

                if(wxPayResultResponse.getReturn_code().equals("SUCCESS") && wxPayResultResponse.getOut_trade_no().startsWith(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE)){

                    BaseResponse<List<PayChannelItemVO>> resp =  payQueryProvider.getAllChannelItem();
                    List<PayChannelItemVO> payChannelItemVOS = resp.getContext();

                    //订单金额
                    BigDecimal total_amount = new BigDecimal(wxPayResultResponse.getCash_fee()).divide(BigDecimal.valueOf(100));
                    //支付终端类型
                    String type = wxPayResultResponse.getTrade_type();
                    if (type.equals("APP")) {
                        type = "wx_app";
                    } else if (type.equals("JSAPI")) {
                        type = "js_api";
                    } else if (type.equals("MWEB")) {
                        type = "wx_mweb";
                    }
                    if(CollectionUtils.isNotEmpty(payChannelItemVOS)){
                        String finalType = type;
                        PayChannelItemVO payChannelItemVO = payChannelItemVOS.stream().filter(x -> x.getIsOpen().equals(IsOpen.YES)
                                && x.getCode().equals(finalType)
                        ).findFirst().orElse(new PayChannelItemVO());
                        type = payChannelItemVO.getId() + "";
                    }
                    // 处理付费会员回调业务
                    StringBuilder key = new StringBuilder();
                    key.append(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE).append("_").append(wxPayResultResponse.getOut_trade_no());

                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.setValueSerializer(new StringRedisSerializer());
                    Object o = redisTemplate.opsForValue().get(key.toString());
                    PaidCardRedisDTO paidCardRedisDTO = JSON.parseObject(o.toString(), PaidCardRedisDTO.class);
                    paidCardRedisDTO.setPayType(0);
                    paidCardCallBack(paidCardRedisDTO, total_amount.toString(), type, wxPayResultResponse.getTransaction_id(), wxPayResultResponse.getMch_id());
                }else{
                    payCallBackTaskService.payCallBack(TradePayOnlineCallBackRequest.builder().payCallBackType(PayCallBackType.WECAHT)
                            .wxPayCallBackResultStr(retXmlStr)
                            .storeId(storeId)
                            .wxPayCallBackResultXmlStr(retXml.toString())
                            .build());
                }


            } catch (RejectedExecutionException e) {
                addPayCallBackResult(PayCallBackResultAddRequest.builder()
                        .businessId(wxPayResultResponse.getOut_trade_no())
                        .resultContext(retXmlStr)
                        .resultXml(retXml.toString())
                        .resultStatus(PayCallBackResultStatus.TODO)
                        .errorNum(0)
                        .payType(PayCallBackType.WECAHT)
                        .build());
            }

        } catch (Exception e) {
            log.error("微信异步通知处理失败:", e);
            result = "fail";
            throw e;
        } finally {
            // 异步消息接收后处理结果返回微信
            wxCallbackResultHandle(response, result);
            log.info("支付成功回调 微信支付异步通知 end result:{}, 微信交易号：{} 订单号:{}", result, wxPayResultResponse.getTransaction_id(), wxPayResultResponse.getOut_trade_no());
        }
    }

    /**
     * @Author lvzhenwei
     * @Description 保存支付回调结果
     * @Date 14:08 2020/7/20
     * @Param [resultAddRequest]
     * @return void
     **/
    private void addPayCallBackResult(PayCallBackResultAddRequest resultAddRequest){
        try{
            payCallBackResultProvider.add(resultAddRequest);
        } catch (SbcRuntimeException e) {
            log.error("addPayCallBackResult error ", e);
            //business_id唯一索引报错捕获，不影响流程处理
            if(!e.getErrorCode().equals(ErrorCodeConstant.PAY_CALL_BACK_RESULT_EXIT)){
                throw e;
            }
            e.printStackTrace();
        }
    }

    /**
     * 微信支付退款成功异步回调
     * @param request
     * @param response
     */
    @ApiOperation(value = "微信支付退款成功异步回调")
    @RequestMapping(value = "/WXPayRefundSuccessCallBack/{storeId}", method = {RequestMethod.POST, RequestMethod.GET})
    @GlobalTransactional
    public void wxPayRefundSuccessCallBack(HttpServletRequest request, HttpServletResponse response, @PathVariable("storeId") Long storeId) throws IOException {
        PayGatewayConfigResponse payGatewayConfig = payQueryProvider.getGatewayConfigByGateway(new
                GatewayConfigByGatewayRequest(PayGatewayEnum.WECHAT, storeId)).getContext();
        String apiKey = payGatewayConfig.getApiKey();
        InputStream inStream;
        String refund_status = "";
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            // 获取微信调用我们notify_url的返回信息
            String result = new String(outSteam.toByteArray(), "utf-8");
            log.info("支付退款回调 微信支付异步通知回调结果 {} ", result);
            // 关闭流
            outSteam.close();
            inStream.close();

            // xml转换为map
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            WxPayRefundCallBackResponse refundCallBackResponse = (WxPayRefundCallBackResponse) WXPayUtil.
                    mapToObject(map, WxPayRefundCallBackResponse.class);
            log.info("支付退款回调 微信支付异步通知回调结果解析信息 {} ", JSON.toJSONString(refundCallBackResponse));
            if (WXPayConstants.SUCCESS.equalsIgnoreCase(refundCallBackResponse.getReturn_code())) {
                if (refundCallBackResponse.getAppid().equals(payGatewayConfig.getOpenPlatformAppId())) {
                    apiKey = payGatewayConfig.getOpenPlatformApiKey();
                }
//                log.info("refund:微信退款----返回成功");
                /** 以下字段在return_code为SUCCESS的时候有返回： **/
                // 加密信息：加密信息请用商户秘钥进行解密，详见解密方式
                String req_info = refundCallBackResponse.getReq_info();
                /**
                 * 解密方式
                 * 解密步骤如下：
                 * （1）对加密串A做base64解码，得到加密串B
                 * （2）对商户key做md5，得到32位小写key* ( key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 )
                 * （3）用key*对加密串B做AES-256-ECB解密（PKCS7Padding）
                 */
                java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
                byte[] b = decoder.decode(req_info);
                SecretKeySpec key = new SecretKeySpec(WXPayUtil.MD5(apiKey).toLowerCase().getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
                cipher.init(Cipher.DECRYPT_MODE, key);
                String resultStr = new String(cipher.doFinal(b), "utf-8");

                // log.info("refund:解密后的字符串:" + resultStr);
                Map<String, String> aesMap = WXPayUtil.xmlToMap(resultStr);
                WxPayRefundCallBackDataResponse dataResponse = (WxPayRefundCallBackDataResponse) WXPayUtil.
                        mapToObject(aesMap, WxPayRefundCallBackDataResponse.class);

                /** 以下为返回的加密字段： **/
                //  商户退款单号  是   String(64)  1.21775E+27 商户退款单号
                String out_refund_no = dataResponse.getOut_refund_no();
                //  退款状态   SUCCESS SUCCESS-退款成功、CHANGE-退款异常、REFUNDCLOSE—退款关闭
                refund_status = dataResponse.getRefund_status();

                if (refund_status.equals(WXPayConstants.SUCCESS)) {
                    PayTradeRecordResponse payTradeRecord =
                            payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest
                                    (out_refund_no)).getContext();

                    ReturnOrderVO returnOrder;
                    if (out_refund_no.startsWith("RT")) {
                        returnOrder = returnOrderQueryProvider.listByCondition(ReturnOrderByConditionRequest.builder()
                                .businessTailId(payTradeRecord.getBusinessId()).build()).getContext().getReturnOrderList().get(0);
                    } else {
                        returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder()
                                .rid(payTradeRecord.getBusinessId()).build()).getContext();
                    }
                    RefundOrderByReturnCodeResponse refundOrder =
                            refundOrderQueryProvider.getByReturnOrderCode(new RefundOrderByReturnOrderCodeRequest(out_refund_no)).getContext();
                    if (out_refund_no.startsWith("RT")) {
                        refundOrder.setRefundChannel(RefundChannel.TAIL);
                    }
                    Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name("UNIONB2B")
                            .platform(Platform.THIRD).build();
                    //微信支付异步回调添加交易数据

                    //异步回调添加交易数据
                    PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
                    //微信支付订单号--及流水号
                    payTradeRecordRequest.setTradeNo(dataResponse.getRefund_id());
                    //商户订单号--业务id(商品退单号)
                    payTradeRecordRequest.setBusinessId(dataResponse.getOut_refund_no());

                    payTradeRecordRequest.setResult_code(dataResponse.getRefund_status());
                    payTradeRecordRequest.setApplyPrice(new BigDecimal(dataResponse.getRefund_fee()).
                            divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
                    payTradeRecordRequest.setPracticalPrice(new BigDecimal(dataResponse.getTotal_fee()).
                            divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
                    //TODO duanlsh
                    payTradeRecordRequest.setAppId(dataResponse.getRefund_account());
                    payProvider.wxPayCallBack(payTradeRecordRequest);
                    returnOrderProvider.onlineRefund(
                            ReturnOrderOnlineRefundRequest.builder().operator(operator)
                                    .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
                                    .refundOrder(KsBeanUtil.convert(refundOrder, RefundOrderDTO.class)).build());

                }
            } else {
                log.error("支付退款回调 微信支付异步通知失败" + refundCallBackResponse.getReturn_msg());
            }
        } catch (Exception e) {
            log.error("支付退款回调 微信支付异步通知解析异常", e);
        } finally {
            wxCallbackResultHandle(response, refund_status);
        }
    }

    private PublicKey getPubKey(String pubKeyString) throws Exception {
        pubKeyString = pubKeyString.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return publicKey;
    }


    /*
     * @Description:  支付宝支付回调
     * @Param:
     * @Author: Bob
     * @Date: 2019-02-26 12:00
     */
//    @ApiOperation(value = "支付宝回调方法")
//    @RequestMapping(value = "/aliPayCallBack1/{storeId}", method = RequestMethod.POST)
//    @GlobalTransactional
//    public void aliPayCallBack(HttpServletRequest request, HttpServletResponse response,@PathVariable("storeId") Long storeId) throws IOException {
//        log.info("===============支付宝回调开始==============");
//        GatewayConfigByGatewayRequest gatewayConfigByGatewayRequest = new GatewayConfigByGatewayRequest();
//        gatewayConfigByGatewayRequest.setGatewayEnum(PayGatewayEnum.ALIPAY);
//        gatewayConfigByGatewayRequest.setStoreId(storeId);
//        //查询支付宝配置信息
//        PayGatewayConfigResponse payGatewayConfigResponse =
//                payQueryProvider.getGatewayConfigByGateway(gatewayConfigByGatewayRequest).getContext();
//        //支付宝公钥
//        String aliPayPublicKey = payGatewayConfigResponse.getPublicKey();
//        Map<String, String> params = new HashMap<String, String>();
//        Map<String, String[]> requestParams = request.getParameterMap();
//        //返回的参数放到params中
//        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//            String name = iter.next();
//            String[] values = requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            params.put(name, valueStr);
//        }
//
//        //支付和退款公用一个回调，所以要判断回调的类型
//        if (params.containsKey("refund_fee")) {
//            //退款只有app支付的订单有回调，退款的逻辑在同步方法中已经处理了，这儿不再做处理
//            log.info("APP退款回调,单号：{}", params.containsKey("out_trade_no"));
//            try {
//                response.getWriter().print("success");
//                response.getWriter().flush();
//                response.getWriter().close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//
//            boolean signVerified = false;
//            try {
//                signVerified = AlipaySignature.rsaCheckV1(params, aliPayPublicKey, "UTF-8", "RSA2"); //调用SDK验证签名
//            } catch (AlipayApiException e) {
//                log.error("支付宝回调签名校验异常：", e);
//            }
//            if (signVerified) {
//                try {
//                    //商户订单号
//                    String out_trade_no = new String(request.getParameter("out_trade_no")
//                            .getBytes("ISO-8859-1"), "UTF-8");
//                    //支付宝交易号
//                    String trade_no = new String(request.getParameter("trade_no")
//                            .getBytes("ISO-8859-1"), "UTF-8");
//                    //交易状态
//                    String trade_status = new String(request.getParameter("trade_status")
//                            .getBytes("ISO-8859-1"), "UTF-8");
//                    //订单金额
//                    String total_amount = new String(request.getParameter("total_amount")
//                            .getBytes("ISO-8859-1"), "UTF-8");
//                    //支付终端类型
//                    String type = new String(request.getParameter("passback_params")
//                            .getBytes("ISO-8859-1"), "UTF-8");
//
//                    boolean isMergePay = isMergePayOrder(out_trade_no);
//                    log.info("-------------支付回调,单号：{}，流水：{}，交易状态：{}，金额：{}，是否合并支付：{}------------",
//                            out_trade_no, trade_no, trade_status, total_amount, isMergePay);
//                    String lockName;
//                    //非组合支付，则查出该单笔订单。
//                    if (!isMergePay) {
//                        if(out_trade_no.startsWith(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE)){
//                            // 说明是付费会员支付回调
//                            lockName = out_trade_no;
//                        }else{
//                            TradeVO tradeVO;
//                            if (isTailPayOrder(out_trade_no)) {
//                                tradeVO = tradeQueryProvider.listAll(TradeListAllRequest.builder().tradeQueryDTO(TradeQueryDTO.builder().tailOrderNo(out_trade_no).build()).build()).getContext().getTradeVOList().get(0);
//                            } else {
//                                tradeVO =
//                                        tradeQueryProvider.getById(new TradeGetByIdRequest(out_trade_no)).getContext().getTradeVO();
//                            }
//                            // 锁资源：无论是否组合支付，都锁父单号，确保串行回调
//                            lockName = tradeVO.getParentId();
//                        }
//
//                    } else {
//                        lockName = out_trade_no;
//                    }
//                    Operator operator =
//                            Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name(PayGatewayEnum.ALIPAY.name())
//                                    .account(PayGatewayEnum.ALIPAY.name()).platform(Platform.THIRD).build();
//                    //redis锁，防止同一订单重复回调
//                    RLock rLock = redissonClient.getFairLock(lockName);
//                    rLock.lock();
//
//                    //执行
//                    try {
//                        List<TradeVO> trades = new ArrayList<>();
//                        //查询交易记录
//                        TradeRecordByOrderCodeRequest tradeRecordByOrderCodeRequest =
//                                new TradeRecordByOrderCodeRequest(out_trade_no);
//                        PayTradeRecordResponse recordResponse =
//                                payQueryProvider.getTradeRecordByOrderCode(tradeRecordByOrderCodeRequest).getContext();
//                        if (isMergePay) {
//                            /*
//                             * 合并支付
//                             * 查询订单是否已支付或过期作废
//                             */
//                            trades = tradeQueryProvider.getListByParentId(
//                                    new TradeListByParentIdRequest(out_trade_no)).getContext().getTradeVOList();
//                            //订单合并支付场景状态采样
//                            boolean paid =
//                                    trades.stream().anyMatch(i -> i.getTradeState().getPayState() == PayState.PAID);
//
//                            boolean cancel =
//                                    trades.stream().anyMatch(i -> i.getTradeState().getFlowState() == FlowState.VOID);
//                            //订单的支付渠道。17、18、19是我们自己对接的支付宝渠道， 表：pay_channel_item
//                            if (cancel || (paid && recordResponse.getChannelItemId() != 17L && recordResponse.getChannelItemId()
//                                    != 18L && recordResponse.getChannelItemId() != 19L)) {
//                                //重复支付，直接退款
//                                alipayRefundHandle(out_trade_no, total_amount);
//                            } else {
//                                alipayCallbackHandle(out_trade_no, trade_no, trade_status, total_amount, type,
//                                        operator, trades, true, recordResponse);
//                            }
//
//                        } else {
//                            if(out_trade_no.startsWith(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE)){
//                                // 处理付费会员回调业务
//                                StringBuilder key = new StringBuilder();
//                                key.append(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE)
//                                        .append("_")
//                                        .append(out_trade_no);
//
//                                String result = redisTemplate.opsForValue().get(key.toString()).toString();
//                                PaidCardRedisDTO paidCardRedisDTO = JSON.parseObject(result, PaidCardRedisDTO.class);
//                                //paidCardCallBack(paidCardRedisDTO,total_amount,type);
//                            }else{
//                                //单笔支付
//                                TradeVO tradeVO;
//                                if (isTailPayOrder(out_trade_no)) {
//                                    tradeVO = tradeQueryProvider.listAll(TradeListAllRequest.builder().tradeQueryDTO(TradeQueryDTO.builder().tailOrderNo(out_trade_no).build()).build()).getContext().getTradeVOList().get(0);
//                                } else {
//                                    tradeVO = tradeQueryProvider.getById(new TradeGetByIdRequest(out_trade_no)).getContext().getTradeVO();
//                                }
//                                if (tradeVO.getTradeState().getFlowState() == FlowState.VOID || (tradeVO.getTradeState()
//                                        .getPayState() == PayState.PAID && recordResponse.getChannelItemId() != 17L && recordResponse.getChannelItemId()
//                                        != 18L && recordResponse.getChannelItemId() != 19L)) {
//                                    //同一批订单重复支付或过期作废，直接退款
//                                    alipayRefundHandle(out_trade_no, total_amount);
//                                } else {
//                                    trades.add(tradeVO);
//                                    alipayCallbackHandle(out_trade_no, trade_no, trade_status, total_amount, type,
//                                            operator, trades, false, recordResponse);
//                                }
//                            }
//                        }
//                    } finally {
//                        //解锁
//                        rLock.unlock();
//                    }
//                } catch (UnsupportedEncodingException e) {
//                    log.error("支付宝回调字节流转码异常：", e);
//                    throw e;
//                } catch (Exception e) {
//                    log.error("支付宝回调异常：", e);
//                    throw e;
//                }
//                try {
//                    response.getWriter().print("success");
//                    response.getWriter().flush();
//                    response.getWriter().close();
//                    log.info("支付回调返回success");
//                } catch (IOException e) {
//                    log.error("支付宝回调异常", e);
//                    throw e;
//                }
//            } else {//验证失败
//                log.info("支付回调签名校验失败,单号：{}", request.getParameter("out_trade_no"));
//                try {
//                    response.getWriter().print("failure");
//                    response.getWriter().flush();
//                    response.getWriter().close();
//                } catch (IOException e) {
//                    log.error("支付宝回调异常", e);
//                    throw e;
//                }
//            }
//        }
//    }

    private void paidCardCallBack(PaidCardRedisDTO paidCardRedisDTO, String total_amount, String type, String transactionId, String appId) {
        String businessId = paidCardRedisDTO.getBusinessId();
        // 查询在流水表中是否存在
        PaidCardBuyRecordByIdResponse context = paidCardBuyRecordQueryProvider.getById(PaidCardBuyRecordByIdRequest.builder()
                .payCode(businessId)
                .build()).getContext();

        if(Objects.nonNull(context.getPaidCardBuyRecordVO())){
            return;
        }
        // 执行业务回调
        BaseResponse response = paidCardSaveProvider.dealPayCallBack(paidCardRedisDTO);
       if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
           // 修改支付流水信息
           //异步回调添加交易数据
           PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
           //流水号
           payTradeRecordRequest.setTradeNo(transactionId);
           //商品订单号
           payTradeRecordRequest.setBusinessId(paidCardRedisDTO.getBusinessId());
           payTradeRecordRequest.setResult_code("SUCCESS");
           payTradeRecordRequest.setPracticalPrice(new BigDecimal(total_amount));
           payTradeRecordRequest.setChannelItemId(Long.valueOf(type));
           payTradeRecordRequest.setAppId(appId);
           //添加交易数据（与微信共用）
           payProvider.wxPayCallBack(payTradeRecordRequest);
       }
    }

    @ApiOperation(value = "支付宝回调方法")
    @RequestMapping(value = "/aliPayCallBack/{storeId}", method = RequestMethod.POST)
    @GlobalTransactional
    public void aliPayBack(HttpServletRequest request, HttpServletResponse response ,@PathVariable("storeId") Long storeId) throws Exception {
        try{
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            //返回的参数放到params中
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            String aliPayResultStr = JSONObject.toJSONString(params);
            //支付和退款公用一个回调，所以要判断回调的类型
            if (params.containsKey("refund_fee")) {
                //退款只有app支付的订单有回调，退款的逻辑在同步方法中已经处理了，这儿不再做处理
                log.info("支付退款回调 支付宝支付异步通知回调结果解析信息 {}", params.containsKey("out_trade_no"));
                try {
                    response.getWriter().print("success");
                    response.getWriter().flush();
                    response.getWriter().close();
                } catch (IOException e) {
                    log.error("PayCallBackController aliPayBack exception", e);
                }
            } else {
                log.info("支付回调 支付宝支付异步通知回调结果解析信息 {}", aliPayResultStr);
                //如果线程池队列已满，则采取拒绝策略（AbortPolicy），抛出RejectedExecutionException异常，则将对应的回调改为处理失败，然后通过定时任务处理补偿
                try {
                    addPayCallBackResult(PayCallBackResultAddRequest.builder()
                                .businessId(out_trade_no)
                                .resultContext(aliPayResultStr)
                                .resultStatus(PayCallBackResultStatus.HANDLING)
                                .errorNum(0)
                                .payType(PayCallBackType.ALI)
                                .build());
                    if(out_trade_no.startsWith(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE)){
                        //订单金额
                        String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
                        //支付终端类型
                        String type = new String(request.getParameter("passback_params").getBytes("ISO-8859-1"), "UTF-8");
                        //支付宝交易流水
                        String trade_no = new String(params.get("trade_no").getBytes("ISO-8859-1"), "UTF-8");

                        String appId = new String(params.get("app_id").getBytes("ISO-8859-1"), "UTF-8");

                        // 处理付费会员回调业务
                        StringBuilder key = new StringBuilder();
                        key.append(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE).append("_").append(out_trade_no);
                        redisTemplate.setKeySerializer(new StringRedisSerializer());
                        redisTemplate.setValueSerializer(new StringRedisSerializer());
                        Object o = redisTemplate.opsForValue().get(key.toString());
                        PaidCardRedisDTO paidCardRedisDTO = JSON.parseObject(o.toString(), PaidCardRedisDTO.class);
                        paidCardRedisDTO.setPayType(1);
                        log.info("支付成功后回调 支付宝 付费会员回调逻辑 begin out_trade_no:{}", out_trade_no);
                        paidCardCallBack(paidCardRedisDTO, total_amount, type, trade_no, appId);
                        log.info("支付成功后回调 支付宝 付费会员回调逻辑 end out_trade_no:{}", out_trade_no);
                    }else{
                        payCallBackTaskService.payCallBack(TradePayOnlineCallBackRequest.builder()
                                .payCallBackType(PayCallBackType.ALI)
                                .aliPayCallBackResultStr(aliPayResultStr)
                                .build());
                    }

                    try {
                        response.getWriter().print("success");
                        response.getWriter().flush();
                        response.getWriter().close();
                    } catch (IOException e) {
                        log.error("支付回调 支付宝支付异步通知异常", e);
                        throw e;
                    }
                } catch (SbcRuntimeException e) {
                    //business_id唯一索引报错捕获，不影响流程处理
                    log.error("支付回调 支付宝支付异步通知回调执行异常 ", e);
                    if(!e.getErrorCode().equals(ErrorCodeConstant.PAY_CALL_BACK_RESULT_EXIT)){
                        throw e;
                    }

                } catch (RejectedExecutionException e) {
                    addPayCallBackResult(PayCallBackResultAddRequest.builder()
                            .businessId(out_trade_no)
                            .resultContext(aliPayResultStr)
                            .resultStatus(PayCallBackResultStatus.TODO)
                            .errorNum(0)
                            .payType(PayCallBackType.ALI)
                            .build());
                    try {
                        response.getWriter().print("success");
                        response.getWriter().flush();
                        response.getWriter().close();
                    } catch (IOException iOE) {
                        log.error("支付回调 支付宝支付异步通知写入异常", iOE);
                        throw e;
                    }
                }
            }
        }catch (Exception e){
            log.error("支付回调 支付宝支付异步通知回调异常信息", e);
            try {
                response.getWriter().print("failure");
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException iOE) {
                log.error("支付回调 支付宝支付异步通知回调写入异常", iOE);
                throw e;
            }
        }
    }

    /**
     * 微信支付--微信企业付款到零钱
     * @return
     */
    @RequestMapping(value = "/wxPayCompanyPayment", method = RequestMethod.POST)
    public BaseResponse<WxPayCompanyPaymentRsponse> wxPayCompanyPayment(@RequestBody WxPayCompanyPaymentInfoRequest request) {
        /*request.setPartner_trade_no("12312312");
        request.setOpenid("oI2m-1MF1vrtu5PIWm2v3G5ln_K0");
        request.setCheck_name("FORCE_CHECK");
        request.setRe_user_name("吕振伟");
        request.setAmount("1");
        request.setDesc("万米小店提现");*/
        request.setSpbill_create_ip(HttpUtil.getIpAddr());
        BaseResponse<WxPayCompanyPaymentRsponse> response = wxPayProvider.wxPayCompanyPayment(request);
        return response;
    }

    private boolean isMergePayOrder(String businessId) {
        return businessId.startsWith(GeneratorService._PREFIX_PARENT_TRADE_ID);
    }

    /**
     * 是否是尾款订单号
     *
     * @param businessId
     * @return
     */
    private boolean isTailPayOrder(String businessId) {
        return businessId.startsWith(GeneratorService._PREFIX_TRADE_TAIL_ID);
    }

    private void wxRefundHandle(WxPayResultResponse wxPayResultResponse, String businessId, Long storeId) {
        WxPayRefundInfoRequest refundInfoRequest = new WxPayRefundInfoRequest();

        refundInfoRequest.setStoreId(storeId);
        refundInfoRequest.setOut_refund_no(businessId);
        refundInfoRequest.setOut_trade_no(businessId);
        refundInfoRequest.setTotal_fee(wxPayResultResponse.getTotal_fee());
        refundInfoRequest.setRefund_fee(wxPayResultResponse.getTotal_fee());
        String tradeType = wxPayResultResponse.getTrade_type();
        if (!tradeType.equals("APP")) {
            tradeType = "PC/H5/JSAPI";
        }
        refundInfoRequest.setPay_type(tradeType);
        //重复支付进行退款处理标志
        refundInfoRequest.setRefund_type("REPEATPAY");
        BaseResponse<WxPayRefundResponse> wxPayRefund =
                wxPayProvider.wxPayRefund(refundInfoRequest);
        WxPayRefundResponse wxPayRefundResponse = wxPayRefund.getContext();
        if (wxPayRefundResponse.getResult_code().equals(WXPayConstants.SUCCESS) &&
                wxPayRefundResponse.getResult_code().equals(WXPayConstants.SUCCESS)) {
            //重复支付退款成功处理逻辑
            operateLogMQUtil.convertAndSend("微信支付", "重复支付退款成功",
                    "订单号：" + wxPayResultResponse.getOut_trade_no());
        } else {
            //重复支付退款失败处理逻辑
            operateLogMQUtil.convertAndSend("微信支付", "重复支付退款失败",
                    "订单号：" + wxPayResultResponse.getOut_trade_no());
        }
    }

    private void wxCallbackResultHandle(HttpServletResponse response, String result) throws IOException {
        if (result.equals(WXPayConstants.SUCCESS)) {
            Map<String, String> sucMap = new HashMap<>();
            sucMap.put("return_code", "SUCCESS");
            sucMap.put("return_msg", "OK");
            response.getWriter().write(WXPayUtil.setXML("SUCCESS", "OK"));
            log.info("微信支付异步通知回调成功的消息回复结束");
        } else {
            Map<String, String> failMap = new HashMap<>();
            failMap.put("return_code", "FAIL");
            failMap.put("return_msg", "ERROR");
            response.getWriter().write(WXPayUtil.setXML("FAIL", "ERROR"));
            log.info("微信支付异步通知回调失败的消息回复结束");
        }
    }

//    private void wxPayCallbackHandle(PayGatewayConfigResponse payGatewayConfig, WxPayResultResponse wxPayResultResponse,
//                                     String businessId, List<TradeVO> trades, boolean isMergePay) {
//        //异步回调添加交易数据
//        PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
//        //微信支付订单号--及流水号
//        payTradeRecordRequest.setTradeNo(wxPayResultResponse.getTransaction_id());
//        //商户订单号或父单号
//        payTradeRecordRequest.setBusinessId(businessId);
//        payTradeRecordRequest.setResult_code(wxPayResultResponse.getResult_code());
//        payTradeRecordRequest.setPracticalPrice(new BigDecimal(wxPayResultResponse.getTotal_fee()).
//                divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
//        ChannelItemByGatewayRequest channelItemByGatewayRequest = new ChannelItemByGatewayRequest();
//        channelItemByGatewayRequest.setGatewayName(payGatewayConfig.getPayGateway().getName());
//        PayChannelItemListResponse payChannelItemListResponse =
//                payQueryProvider.listChannelItemByGatewayName(channelItemByGatewayRequest).getContext();
//        List<PayChannelItemVO> payChannelItemVOList =
//                payChannelItemListResponse.getPayChannelItemVOList();
//        String tradeType = wxPayResultResponse.getTrade_type();
//        ChannelItemSaveRequest channelItemSaveRequest = new ChannelItemSaveRequest();
//        String code = "wx_qr_code";
//        if (tradeType.equals("APP")) {
//            code = "wx_app";
//        } else if (tradeType.equals("JSAPI")) {
//            code = "js_api";
//        } else if (tradeType.equals("MWEB")) {
//            code = "wx_mweb";
//        }
//        channelItemSaveRequest.setCode(code);
//        payChannelItemVOList.forEach(payChannelItemVO -> {
//            if (channelItemSaveRequest.getCode().equals(payChannelItemVO.getCode())) {
//                //更新支付项
//                payTradeRecordRequest.setChannelItemId(payChannelItemVO.getId());
//            }
//        });
//        //微信支付异步回调添加交易数据
//        payProvider.wxPayCallBack(payTradeRecordRequest);
//        //订单 支付单 操作信息
//        Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name(PayGatewayEnum.WECHAT.name())
//                .account(PayGatewayEnum.WECHAT.name()).platform(Platform.THIRD).build();
//        payCallbackOnline(trades, operator, isMergePay);
//    }

    /**
     * 线上订单支付回调
     * 订单 支付单 操作信息
     * @return 操作结果
     */
    private void payCallbackOnline(List<TradeVO> trades, Operator operator, boolean isMergePay) {
        //封装回调参数
        List<TradePayCallBackOnlineDTO> reqOnlineDTOList = trades.stream().map(i -> {
            //每笔订单做是否合并支付标识
            i.getPayInfo().setMergePay(isMergePay);
            tradeProvider.update(new TradeUpdateRequest(KsBeanUtil.convert(i, TradeUpdateDTO.class)));
            if (Objects.nonNull(i.getIsBookingSaleGoods()) && i.getIsBookingSaleGoods() && i.getBookingType() == BookingType.EARNEST_MONEY &&
                    StringUtils.isNotEmpty(i.getTailOrderNo()) && StringUtils.isNotEmpty(i.getTailPayOrderId())) {
                //支付单信息
                PayOrderVO payOrder = tradeQueryProvider.getPayOrderById(TradeGetPayOrderByIdRequest
                        .builder().payOrderId(i.getTailPayOrderId()).build()).getContext().getPayOrder();

                return TradePayCallBackOnlineDTO.builder()
                        .trade(KsBeanUtil.convert(i, TradeDTO.class))
                        .payOrderOld(KsBeanUtil.convert(payOrder, PayOrderDTO.class))
                        .build();
            } else {
                //支付单信息
                PayOrderVO payOrder = tradeQueryProvider.getPayOrderById(TradeGetPayOrderByIdRequest
                        .builder().payOrderId(i.getPayOrderId()).build()).getContext().getPayOrder();
                return TradePayCallBackOnlineDTO.builder()
                        .trade(KsBeanUtil.convert(i, TradeDTO.class))
                        .payOrderOld(KsBeanUtil.convert(payOrder, PayOrderDTO.class))
                        .build();
            }

        }).collect(Collectors.toList());
        //批量回调
        tradeProvider.payCallBackOnlineBatch(new TradePayCallBackOnlineBatchRequest(reqOnlineDTOList,
                operator));

        // 订单支付回调同步供应商订单状态
        //this.providerTradePayCallBack(trades);

    }

    /**
     * 支付宝退款处理
     *
     * @param out_trade_no
     * @param total_amount
     */
    private void alipayRefundHandle(String out_trade_no, String total_amount) {
        //调用退款接口。直接退款。不走退款流程，没有交易对账，只记了操作日志
        AliPayRefundResponse aliPayRefundResponse =
                aliPayProvider.aliPayRefund(AliPayRefundRequest.builder().businessId(out_trade_no)
                        .amount(new BigDecimal(total_amount)).description("重复支付退款").build()).getContext();

        if (aliPayRefundResponse.getAlipayTradeRefundResponse().isSuccess()) {
            operateLogMQUtil.convertAndSend("支付宝退款", "重复支付、超时订单退款",
                    "订单号：" + out_trade_no);
        }
        log.info("支付宝重复支付、超时订单退款,单号：{}", out_trade_no);
    }

//    private void alipayCallbackHandle(String out_trade_no, String trade_no, String trade_status, String total_amount,
//                                      String type, Operator operator, List<TradeVO> trades, boolean isMergePay,
//                                      PayTradeRecordResponse recordResponse) {
//        if (recordResponse.getApplyPrice().compareTo(new BigDecimal(total_amount)) == 0 && trade_status.equals(
//                "TRADE_SUCCESS")) {
//            //异步回调添加交易数据
//            PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
//            //流水号
//            payTradeRecordRequest.setTradeNo(trade_no);
//            //商品订单号
//            payTradeRecordRequest.setBusinessId(out_trade_no);
//            payTradeRecordRequest.setResult_code("SUCCESS");
//            payTradeRecordRequest.setPracticalPrice(new BigDecimal(total_amount));
//            payTradeRecordRequest.setChannelItemId(Long.valueOf(type));
//            //添加交易数据（与微信共用）
//            payProvider.wxPayCallBack(payTradeRecordRequest);
//            payCallbackOnline(trades, operator, isMergePay);
//            log.info("支付回调成功,单号：{}", out_trade_no);
//        }
//    }
//
//    /**
//     * 订单支付回调同步供应商订单状态
//     * @param trades
//     */
//    private void providerTradePayCallBack(List<TradeVO> trades) {
//        if (CollectionUtils.isNotEmpty(trades)) {
//            trades.forEach(parentTradeVO -> {
//                String parentId = parentTradeVO.getId();
//                BaseResponse<TradeListByParentIdResponse> supplierListByParentId =
//                        providerTradeQueryProvider.getProviderListByParentId(TradeListByParentIdRequest.builder().parentTid(parentId).build());
//                if (Objects.nonNull(supplierListByParentId.getContext()) && CollectionUtils.isNotEmpty(supplierListByParentId.getContext().getTradeVOList())) {
//                    supplierListByParentId.getContext().getTradeVOList().forEach(childTradeVO->{
//                        childTradeVO.getTradeState().setPayState(PayState.PAID);
//                        TradeUpdateRequest tradeUpdateRequest = new TradeUpdateRequest(KsBeanUtil.convert(childTradeVO, TradeUpdateDTO.class));
//                        providerTradeProvider.providerUpdate(tradeUpdateRequest);
//                    });
//                }
//            });
//        }
//    }

    @ApiOperation(value = "获取未支付订单，拼装对应支付报文信息")
    @RequestMapping(value = "/getNotPaidTrade", method = {RequestMethod.POST, RequestMethod.GET})
    public void getNotPaidTrade(){
        int pageSize = 2000;
        String xmlStr = "<xml><appid><![CDATA[wxb67fac0bbb6b4031]]></appid><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1489104242]]></mch_id><nonce_str><![CDATA[jr4itIa2Kin4j9p5VRV1UDAmZLsZGA8P]]></nonce_str><openid><![CDATA[o6wq55YZ9xrgyQwQeka6btYt5HOQ]]></openid><out_trade_no><![CDATA[%s]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[DA4F5784AEA7A17B608267EF8131A87A]]></sign><time_end><![CDATA[20200701144442]]></time_end><total_fee>%s</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000616202007015217733087]]></transaction_id></xml>\r\n";
        try {
            String savePath = "/data";
            File file = new File(savePath);
            if (!file.exists()) {//保存文件夹不存在则建立
                file.mkdirs();
            }
            File xmlfile = new File("/data/add.txt");
            if(!xmlfile.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(xmlfile);
            TradeQueryDTO tradeQueryRequest = new TradeQueryDTO();
            tradeQueryRequest.setBeginTime(LocalDate.now().toString());
            tradeQueryRequest.setEndTime(LocalDate.now().toString());
            tradeQueryRequest.setTradeState(TradeStateDTO.builder().payState(PayState.NOT_PAID).build());
            tradeQueryRequest.setPageSize(pageSize);
            TradePageCriteriaResponse tradePageCriteriaResponse = tradeQueryProvider.pageCriteria(TradePageCriteriaRequest.builder()
                    .tradePageDTO(tradeQueryRequest)
                    .build()).getContext();
            long totalPages = tradePageCriteriaResponse.getTradePage().getTotal();
            log.info("未支付订单总条数：total=:{}",totalPages);
            int pageNum = (int) Math.ceil((double )(totalPages/pageSize))+1;
            if(totalPages>1){
                tradePageCriteriaResponse.getTradePage().getContent().forEach(tradeVO -> {
                    String totalPrice = tradeVO.getTradePrice().getTotalPrice().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toString();
                    String xmlStrNew = String.format(xmlStr,tradeVO.getId(),totalPrice);
                    try {
                        out.write(xmlStrNew.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                for(int num=1;num<pageNum;num++){
                    tradeQueryRequest.setPageNum(num);
                    tradePageCriteriaResponse = tradeQueryProvider.pageCriteria(TradePageCriteriaRequest.builder()
                            .tradePageDTO(tradeQueryRequest)
                            .build()).getContext();
                    tradePageCriteriaResponse.getTradePage().getContent().forEach(tradeVO -> {
                        String totalPrice = tradeVO.getTradePrice().getTotalPrice().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toString();
                        String xmlStrNew = String.format(xmlStr,tradeVO.getId(),totalPrice);
                        try {
                            out.write(xmlStrNew.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信支付异步回调
     * 通过信号量控制回调并发数，代码中默认8，线上配置16
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "微信支付异步回调")
    @RequestMapping(value = "/paySucLockForBatch/{storeId}", method = {RequestMethod.POST, RequestMethod.GET})
    public void paySucLockForBatch(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable("storeId") Long storeId) throws Exception {
        try {
            paySucForBacth(request, response, storeId);
        } catch (Exception e) {
            response.getWriter().write(WXPayUtil.setXML("FAIL", "ERROR"));
        }
    }

    /**
     * 微信支付异步回调
     * @param request
     * @param response
     */
    @GlobalTransactional
    public void paySucForBacth(HttpServletRequest request, HttpServletResponse response ,@PathVariable("storeId") Long storeId) throws Exception {
        log.info("======================微信支付异步通知回调start======================");
        //支付回调结果
        String result = WXPayConstants.SUCCESS;
        //微信回调结果参数对象
        WxPayResultResponse wxPayResultResponse = new WxPayResultResponse();
        try {
            //获取回调数据输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder retXml = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                retXml.append(line);
            }
            //微信支付异步回调结果xml
            log.info("微信支付异步通知回调结果xml====" + retXml);
            log.info("微信支付异步通知回调结果request ===={}", request);
            log.info("微信支付异步通知回调结果request xml====" + request.getParameter(
                    "xml"));
            String retXmlStr;
            retXmlStr = request.getParameter("xml").replaceAll("&lt;","<").replaceAll("&gt;",">");
//            String retXmlStr = "<xml><appid><![CDATA[wxb67fac0bbb6b4031]]></appid><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1489104242]]></mch_id><nonce_str><![CDATA[jr4itIa2Kin4j9p5VRV1UDAmZLsZGA8P]]></nonce_str><openid><![CDATA[o6wq55YZ9xrgyQwQeka6btYt5HOQ]]></openid><out_trade_no><![CDATA[O202007011444302542]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[DA4F5784AEA7A17B608267EF8131A87A]]></sign><time_end><![CDATA[20200701144442]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000616202007015217733087]]></transaction_id></xml>";
            retXmlStr = retXmlStr.replaceAll("<coupon_id_[0-9]{0,11}[^>]*>(.*?)</coupon_id_[0-9]{0,11}>", "");
            retXmlStr = retXmlStr.replaceAll("<coupon_type_[0-9]{0,11}[^>]*>(.*?)</coupon_type_[0-9]{0,11}>", "");
            retXmlStr = retXmlStr.replaceAll("<coupon_fee_[0-9]{0,11}[^>]*>(.*?)</coupon_fee_[0-9]{0,11}>", "");

            //将回调数据写入数据库
            XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
            xStream.alias("xml", WxPayResultResponse.class);
            wxPayResultResponse = (WxPayResultResponse) xStream.fromXML(retXmlStr);
            //如果线程池队列已满，则采取拒绝策略（AbortPolicy），抛出RejectedExecutionException异常，则将对应的回调改为处理失败，然后通过定时任务处理补偿
            try {
                addPayCallBackResult(PayCallBackResultAddRequest.builder()
                        .businessId(wxPayResultResponse.getOut_trade_no())
                        .resultXml(retXmlStr)
                        .resultContext(retXmlStr)
                        .resultStatus(PayCallBackResultStatus.HANDLING)
                        .errorNum(0)
                        .payType(PayCallBackType.WECAHT)
                        .build());
                payCallBackTaskService.payCallBack(TradePayOnlineCallBackRequest.builder().payCallBackType(PayCallBackType.WECAHT)
                        .wxPayCallBackResultStr(retXmlStr)
                        .wxPayCallBackResultXmlStr(retXmlStr)
                        .build());
            } catch (RejectedExecutionException e) {
                addPayCallBackResult(PayCallBackResultAddRequest.builder()
                        .businessId(wxPayResultResponse.getOut_trade_no())
                        .resultContext(retXmlStr)
                        .resultXml(retXmlStr)
                        .resultStatus(PayCallBackResultStatus.TODO)
                        .errorNum(0)
                        .payType(PayCallBackType.WECAHT)
                        .build());
            }
        } catch (Exception e) {
            log.error("微信异步通知处理失败:", e);
            result = "fail";
            throw e;
        } finally {
            // 异步消息接收后处理结果返回微信
            wxCallbackResultHandle(response, result);
            log.info("微信异步通知完成：结果=" + result + "，微信交易号=" + wxPayResultResponse.getTransaction_id());
        }
    }
}
