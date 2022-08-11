package com.wanmi.sbc.pay.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wanmi.sbc.common.constant.ErrorCodeConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.AliPayRefundResponse;
import com.wanmi.sbc.pay.api.response.BalanceRefundResponse;
import com.wanmi.sbc.pay.api.response.WxPayRefundResponse;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.bean.enums.TradeType;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.gateway.GatewayProxy;
import com.wanmi.sbc.pay.model.entity.PayRecordResult;
import com.wanmi.sbc.pay.model.entity.PayResult;
import com.wanmi.sbc.pay.model.entity.RefundRecordResult;
import com.wanmi.sbc.pay.model.entity.RefundResult;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGateway;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import com.wanmi.sbc.pay.repository.ChannelItemRepository;
import com.wanmi.sbc.pay.repository.GatewayConfigRepository;
import com.wanmi.sbc.pay.repository.GatewayRepository;
import com.wanmi.sbc.pay.repository.TradeRecordRepository;
import com.wanmi.sbc.pay.unionpay.acp.sdk.AcpService;
import com.wanmi.sbc.pay.unionpay.acp.sdk.SDKConfig;
import com.wanmi.sbc.pay.utils.GeneratorUtils;
import com.wanmi.sbc.pay.utils.PayValidates;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayConstants;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>支付交易service</p>
 * Created by of628-wenzhi on 2017-08-02-下午5:44.
 */
@Service
@Validated
@Slf4j
public class PayService {

    private static final String WXPAYATYPE = "PC/H5/JSAPI"; //微信支付类型--为PC/H5/JSAPI，对应调用参数对应公众平台参数

    private static final String WXPAYAPPTYPE = "APP"; //微信支付类型--为app，对应调用参数对应开放平台参数

    @Resource
    private GatewayProxy proxy;

    @Resource
    private ChannelItemRepository channelItemRepository;

    @Resource
    private TradeRecordRepository recordRepository;

    @Resource
    private GatewayRepository gatewayRepository;

    @Resource
    private GatewayConfigRepository gatewayConfigRepository;

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private WxPayService wxPayService;


    /**
     * 获取支付对象，这里返回到的支付对象实际为支付凭证，由前端获取后通过JS请求第三方支付
     *
     * @param request
     */
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    public Object pay(@Valid PayExtraRequest request) {

        //获取支付渠道项
        PayChannelItem item = getPayChannelItem(request.getChannelItemId(),request.getStoreId());
        if (item.getTerminal() != request.getTerminal()) {
            throw new SbcRuntimeException("K-100202");
        }
        //是否重复支付
        PayTradeRecord record = recordRepository.findByBusinessId(request.getBusinessId());
        if (!Objects.isNull(record)) {
            //如果重复支付，判断状态，已成功状态则做异常提示
            if (record.getStatus() == TradeStatus.SUCCEED) {
                throw new SbcRuntimeException("K-100203");
            } else if (record.getStatus() == TradeStatus.PROCESSING && !Objects.isNull(record.getChargeId())) {
                //未支付状态，跟踪支付结果
                PayRecordResult result = queryPayResult(record);
                if (result.getRecord().getStatus() == TradeStatus.SUCCEED) {
                    //更新记录
                    recordRepository.updateTradeStatusAndPracticalPriceAndFinishTime(
                            result.getRecord().getId(),
                            result.getRecord().getStatus(),
                            result.getRecord().getPracticalPrice(),
                            result.getRecord().getFinishTime()
                    );
                    throw new SbcRuntimeException("K-100203");
                } else if (result.getRecord().getStatus() == TradeStatus.PROCESSING) {
                    //30秒内阻止重复创建
                    Duration duration1 = Duration.between(LocalDateTime.now(), result.getRecord().getCreateTime());
                    if (duration1.toMillis() > -30000) {
                        throw new SbcRuntimeException("K-100207");
                    }
                    if (!Objects.isNull(result.getObject())
                            && Objects.equals(result.getRecord().getChannelItemId(), request.getChannelItemId())) {
                        Duration duration2 = Duration.between(LocalDateTime.now(), result.getTimeExpire());
                        //相同单号和支付渠道下的未支付订单，金额如果不一致则重新创建支付对象，一致且支付凭证未失效，则返回已创建的支付对象
                        if (request.getAmount().compareTo(record.getApplyPrice()) == 0 && duration2.toMillis() > 0) {
                            return result.getObject();
                        }
                    }
                }
            }
        }

        PayValidates.verifyGateway(item.getGateway());

        //调用网关支付
        PayResult result = proxy.pay(request, item);
        //获取支付对象,存入记录
        savePayRecord(request, result);
        return result.getData();
    }

    /**
     * 根据订单号查询支付结果
     *
     * @param tid 业务订单号
     * @return 支付结果
     */
    @Transactional
    public TradeStatus queryPayResult(String tid) {
        PayTradeRecord record = recordRepository.findByBusinessId(tid);
        if (!Objects.isNull(record)) {
            //如果重复支付，判断状态
            if (record.getStatus() == TradeStatus.SUCCEED) {
                return TradeStatus.SUCCEED;
            } else if (record.getStatus() == TradeStatus.PROCESSING && !Objects.isNull(record.getChargeId())) {
                //未支付状态，跟踪支付结果
                PayRecordResult result = queryPayResult(record);
                if (result.getRecord().getStatus() == TradeStatus.SUCCEED) {
                    //更新记录
                    recordRepository.updateTradeStatusAndPracticalPriceAndFinishTime(
                            result.getRecord().getId(),
                            result.getRecord().getStatus(),
                            result.getRecord().getPracticalPrice(),
                            result.getRecord().getFinishTime()
                    );
                    return TradeStatus.SUCCEED;
                } else if (result.getRecord().getStatus() == TradeStatus.PROCESSING
                        && !Objects.isNull(result.getObject())) {
                    return TradeStatus.PROCESSING;
                }
            }
        }
        return null;
    }

    /**
     * 退款
     *
     * @param request
     */
    @GlobalTransactional
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    public Object refund(@Valid RefundRequest request) {
        //重复退款校验
        TradeStatus status = queryRefundResult(request.getRefundBusinessId(), request.getBusinessId());
        if (!Objects.isNull(status)) {
            if (status == TradeStatus.SUCCEED) {
                throw new SbcRuntimeException("K-100104");
            } else if (status == TradeStatus.PROCESSING) {
                throw new SbcRuntimeException("K-100105");
            }
        }

        //未退款或退款失败的退单，调用网关执行退款操作
        PayTradeRecord payRecord = recordRepository.findTopByBusinessIdAndStatus(request.getBusinessId(), TradeStatus.SUCCEED);

        Long storeId = request.getStoreId();
        request.setStoreId(storeId);
        PayGateway payGateway = getPayGatewayNew(payRecord.getAppId(),storeId);
        log.info("PayService refund payRecord :{} item: {}", JSON.toJSONString(payRecord), JSON.toJSONString(payGateway));
        if (payRecord.getChannelItemId().equals(11L)) {
            //银联企业支付退款
//            PayValidates.verifyGateway(payGateway);
            PayTradeRecord record = saveRefundRecord(request, payRecord.getChannelItemId(), payRecord.getAppId());
//            payRecord.setBusinessId(record.getBusinessId());
            record.setTradeNo(payRecord.getTradeNo());
            PayGatewayConfig gatewayConfig = gatewayConfigRepository.queryConfigByNameAndStoreId(PayGatewayEnum.UNIONB2B, storeId);
            Map<String, String> resultMap = unionRefund(record, gatewayConfig);
            log.info(">>>>>>>>>>>>>>>>>>respCode:" + resultMap.get("respCode") + "respMsg:" + resultMap.get("respMsg"));
            if ("00".equals(resultMap.get("respCode"))) {
                record.setTradeNo(resultMap.get("orderId"));
                recordRepository.save(record);
            } else {
                //提交退款申请失败
                //throw new SbcRuntimeException("K-100105");
            }
            return resultMap;
        } else if (payRecord.getChannelItemId().equals(14L) || payRecord.getChannelItemId().equals(15L) ||
                payRecord.getChannelItemId().equals(16L)) {
            //微信支付退款--PC、H5、JSAPI支付对应付退款
            return wxPayRefundForPcH5Jsapi(payRecord.getChannelItemId(), request, payRecord);
        } else if (payRecord.getChannelItemId().equals(20L)) {
            //微信支付--app支付退款
            return wxPayRefundForApp(payRecord.getChannelItemId(), request, payRecord);
        } else if (payRecord.getChannelItemId() == 17L || payRecord.getChannelItemId() == 18L || payRecord.getChannelItemId() == 19L) {
            //支付宝退款
            PayTradeRecord data = saveRefundRecord(request, payRecord.getChannelItemId(), payRecord.getAppId());
            AliPayRefundRequest aliPayRefundRequest = KsBeanUtil.convert(request, AliPayRefundRequest.class);
            aliPayRefundRequest.setAppid(payGateway.getConfig().getAppId());
            aliPayRefundRequest.setAppPrivateKey(payGateway.getConfig().getPrivateKey());
            aliPayRefundRequest.setAliPayPublicKey(payGateway.getConfig().getPublicKey());
            AlipayTradeRefundResponse refundResponse = alipayService.tradeRefund(aliPayRefundRequest);
            log.info("PayService refund refundResponse: {}", JSON.toJSONString(refundResponse));
            AliPayRefundResponse aliPayRefundResponse = new AliPayRefundResponse();
            aliPayRefundResponse.setPayType("ALIPAY");
            aliPayRefundResponse.setAlipayTradeRefundResponse(refundResponse);

            if (refundResponse.isSuccess()) {
                //更新记录
                data.setChargeId(request.getRefundBusinessId());
                data.setTradeNo(refundResponse.getTradeNo());
                data.setAppId(refundResponse.getOpenId());
                data.setFinishTime(LocalDateTime.now());
                data.setStatus(TradeStatus.SUCCEED);
                data.setCallbackTime(LocalDateTime.now());
                data.setPracticalPrice(new BigDecimal(refundResponse.getRefundFee()));
                data.setAppId(payRecord.getAppId());
                recordRepository.save(data);
            } else {
                throw new SbcRuntimeException("K-100211", new Object[]{refundResponse.getSubMsg()});
            }
            return aliPayRefundResponse;
        } else if (payRecord.getChannelItemId() == 21L || payRecord.getChannelItemId() == 22L || payRecord.getChannelItemId() == 23L) {
            //余额支付退款操作
            return balanceRefund(payRecord.getChannelItemId(), request, payRecord.getAppId());
        } else {
            request.setPayObjectId(payRecord.getChargeId());
//            PayValidates.verifyGateway(payGateway);
            PayTradeRecord data = saveRefundRecord(request, payRecord.getChannelItemId(), payRecord.getAppId());
            RefundResult result = proxy.refund(request, payGateway);
            try {
                //更新记录
                data.setChargeId(result.getRefundObjectId());
                data.setTradeNo(result.getTradeNo());
                recordRepository.save(data);
            } catch (Exception e) {
                log.error("After calling the gateway refund operation, the update record fails，" +
                                "request={}," +
                                "result={}",
                        request,
                        result,
                        e
                );
            }
            return result.getData();
        }
    }

    /**
     * @return com.wanmi.sbc.pay.api.response.WxPayRefundResponse
     * @Author lvzhenwei
     * @Description 余额支付订单在线退款
     * @Date 15:59 2019/7/11
     * @Param [item, request, payRecord]
     **/
    private BalanceRefundResponse balanceRefund(Long channelItemId, RefundRequest request, String appId) {
        PayTradeRecord data = saveRefundRecord(request, channelItemId, appId);
        //更新记录
        data.setChargeId(request.getRefundBusinessId());
        data.setFinishTime(LocalDateTime.now());
        data.setStatus(TradeStatus.SUCCEED);
        data.setCallbackTime(LocalDateTime.now());
        data.setPracticalPrice(request.getAmount());
        recordRepository.save(data);
        BalanceRefundResponse balanceRefundResponse = new BalanceRefundResponse();
        balanceRefundResponse.setPayType(PayGatewayEnum.BALANCE.name());
        return balanceRefundResponse;
    }

    /**
     * 微信支付--PC、H5、JSAPI支付对应付退款
     *
     * @param request
     * @param payRecord
     * @return
     */
    private WxPayRefundResponse wxPayRefundForPcH5Jsapi(Long channelItemId, RefundRequest request, PayTradeRecord payRecord) {
//        PayValidates.verifyGateway(item.getGateway());
        PayTradeRecord record = saveRefundRecord(request, channelItemId, payRecord.getAppId());
        record.setTradeNo(payRecord.getTradeNo());
        List<PayGatewayConfig> payGatewayConfigs = gatewayConfigRepository.queryConfigOpenByNameAndStoreId(PayGatewayEnum.WECHAT, request.getStoreId());
        if (CollectionUtils.isEmpty(payGatewayConfigs)) {
            throw new SbcRuntimeException("K-999999", "没有获取到对应的支付配置信息");
        }
        
        PayGatewayConfig gatewayConfig = payGatewayConfigs.get(0);
        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
        refundRequest.setAppid(gatewayConfig.getAppId());
        refundRequest.setMch_id(gatewayConfig.getAccount());
        refundRequest.setNonce_str(WXPayUtil.generateNonceStr());
        refundRequest.setNotify_url(gatewayConfig.getBossBackUrl() + "/tradeCallback/WXPayRefundSuccessCallBack/"+request.getStoreId());
        refundRequest.setOut_refund_no(request.getRefundBusinessId());
        refundRequest.setOut_trade_no(request.getBusinessId());
        refundRequest.setTotal_fee(request.getTotalPrice().multiply(new BigDecimal(100)).
                setScale(0, BigDecimal.ROUND_DOWN).toString());
        refundRequest.setRefund_fee(request.getAmount().multiply(new BigDecimal(100)).
                setScale(0, BigDecimal.ROUND_DOWN).toString());
        try {
            Map<String, String> refundMap = WXPayUtil.objectToMap(refundRequest);
            //获取签名
            String sign = WXPayUtil.generateSignature(refundMap, gatewayConfig.getApiKey());
            refundRequest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        log.info(">>>>>>>>>>>>>>>>>>调用微信退款入参:{}" ,refundRequest);
        WxPayRefundResponse wxPayRefundResponse = wxPayService.wxPayRefund(refundRequest, WXPAYATYPE, request.getStoreId());
//        log.info(">>>>>>>>>>>>>>>>>>调用微信退款返回值:{}" ,wxPayRefundResponse);
        if (wxPayRefundResponse.getReturn_code().equals(WXPayConstants.SUCCESS) &&
                wxPayRefundResponse.getResult_code().equals(WXPayConstants.SUCCESS)) {
            record.setTradeNo(wxPayRefundResponse.getTransaction_id());
            recordRepository.save(record);
        } else {
            //退款失败
            log.info(">>>>>>>>>>>>>>>>>>微信退款失败:return_code" + wxPayRefundResponse.getReturn_code() + "respMsg:" + wxPayRefundResponse.getReturn_msg());
            log.info(">>>>>>>>>>>>>>>>>>微信退款失败:err_code" + wxPayRefundResponse.getErr_code() + "respMsg:" + wxPayRefundResponse.getErr_code_des());
            String errMsg = "退款失败原因：";
            if (!wxPayRefundResponse.getReturn_code().equals(WXPayConstants.SUCCESS)) {
                errMsg = errMsg + wxPayRefundResponse.getReturn_msg() + ";";
            }
            if (!wxPayRefundResponse.getResult_code().equals(WXPayConstants.SUCCESS)) {
                errMsg = errMsg + wxPayRefundResponse.getErr_code_des() + ";";
            }
            throw new SbcRuntimeException("K-100212", new Object[]{errMsg});
        }
        return wxPayRefundResponse;
    }

    /**
     * 微信支付退款--app支付退款
     *
     * @param request
     * @param payRecord
     * @return
     */
    private WxPayRefundResponse wxPayRefundForApp( Long channelItemId, RefundRequest request, PayTradeRecord payRecord) {
        //微信支付退款
//        PayValidates.verifyGateway(item.getGateway());
        PayTradeRecord record = saveRefundRecord(request, channelItemId, payRecord.getAppId());
        record.setTradeNo(payRecord.getTradeNo());
        PayGatewayConfig gatewayConfig = gatewayConfigRepository.queryConfigByNameAndStoreId(PayGatewayEnum.WECHAT, request.getStoreId());
        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
        refundRequest.setAppid(gatewayConfig.getOpenPlatformAppId());
        refundRequest.setMch_id(gatewayConfig.getOpenPlatformAccount());
        refundRequest.setNonce_str(WXPayUtil.generateNonceStr());
        refundRequest.setOut_refund_no(request.getRefundBusinessId());
        refundRequest.setOut_trade_no(request.getBusinessId());
        refundRequest.setTotal_fee(request.getTotalPrice().multiply(new BigDecimal(100)).
                setScale(0, BigDecimal.ROUND_DOWN).toString());
        refundRequest.setRefund_fee(request.getAmount().multiply(new BigDecimal(100)).
                setScale(0, BigDecimal.ROUND_DOWN).toString());
        refundRequest.setNotify_url(gatewayConfig.getBossBackUrl() + "/tradeCallback/WXPayRefundSuccessCallBack/"+request.getStoreId());
        log.info("PayService wxPayRefundForApp refundRequest :{}", JSON.toJSONString(refundRequest));
        try {
            Map<String, String> refundMap = WXPayUtil.objectToMap(refundRequest);
            //获取签名
            String sign = WXPayUtil.generateSignature(refundMap, gatewayConfig.getOpenPlatformApiKey());
            refundRequest.setSign(sign);
        } catch (Exception e) {
            log.error("PayService wxPayRefundForApp singin exception", e);
        }
        WxPayRefundResponse wxPayRefundResponse = wxPayService.wxPayRefund(refundRequest, WXPAYAPPTYPE, request.getStoreId());
        log.info("PayService wxPayRefundForApp wxPayRefundResponse :{}", JSON.toJSONString(wxPayRefundResponse));
        if (Objects.nonNull(wxPayRefundResponse) && wxPayRefundResponse.getResult_code().equals(WXPayConstants.SUCCESS)) {
            record.setTradeNo(wxPayRefundResponse.getTransaction_id());
            recordRepository.save(record);
        } else {
            //提交退款申请失败
            //退款失败
            log.info(">>>>>>>>>>>>>>>>>>微信退款失败:return_code" + wxPayRefundResponse.getReturn_code() + "respMsg:" + wxPayRefundResponse.getReturn_msg());
            log.info(">>>>>>>>>>>>>>>>>>微信退款失败:err_code" + wxPayRefundResponse.getErr_code() + "respMsg:" + wxPayRefundResponse.getErr_code_des());
            String errMsg = "微信支付退款：";
            if (StringUtils.isNotBlank(wxPayRefundResponse.getErr_code())) {
                errMsg = errMsg + wxPayRefundResponse.getErr_code() + ";";
            }
            if (StringUtils.isNotBlank(wxPayRefundResponse.getErr_code_des())) {
                errMsg = errMsg + wxPayRefundResponse.getErr_code_des() + ";";
            }
            throw new SbcRuntimeException("K-100212", new Object[]{errMsg});
        }
        return wxPayRefundResponse;
    }

    /**
     * 根据退单与相关订单号号查询退单退款状态
     *
     * @param  业务退单号
     * @param  业务订单号
     * @return null-无退款记录 | TradeStatus-退款状态
     */
    @Transactional
    public TradeStatus queryRefundResult(String returnTradeId, String tradeId) {

        PayTradeRecord refundRecord = recordRepository.findByBusinessId(returnTradeId);
        if (!Objects.isNull(refundRecord)) {
            if (refundRecord.getStatus() == TradeStatus.SUCCEED) {
                return TradeStatus.SUCCEED;
            } else if (refundRecord.getStatus() == TradeStatus.PROCESSING && !Objects.isNull(refundRecord.getChargeId())) {
                //处理中退单，跟踪状态
                PayTradeRecord payRecord = recordRepository.findTopByBusinessIdAndStatus(tradeId, TradeStatus.SUCCEED);
                RefundRecordResult result = queryRefundResult(refundRecord, payRecord.getChargeId());
                if (result.getRecord().getStatus() == TradeStatus.SUCCEED) {
                    //更新记录
                    recordRepository.updateTradeStatusAndPracticalPriceAndFinishTime(
                            result.getRecord().getId(),
                            result.getRecord().getStatus(),
                            result.getRecord().getPracticalPrice(),
                            result.getRecord().getFinishTime());
                    return TradeStatus.SUCCEED;
                } else if (result.getRecord().getStatus() == TradeStatus.PROCESSING) {
                    return TradeStatus.PROCESSING;
                } else {
                    return TradeStatus.FAILURE;
                }
            }
        }
        return null;
    }


    /**
     * 交易回调,支付模块不执行业务触发，只做更新交易记录操作
     *
     * @param request
     */
    public void callback(@Valid TradeCallbackRequest request) {
        System.err.println("----------------------------1----------------------------------------");
        System.err.println(request.getTradeStatus());
        System.err.println("----------------------------2----------------------------------------");
        PayTradeRecord record = recordRepository.findByChargeId(request.getObjectId());
        if (record.getTradeType() != request.getTradeType()) {
            throw new SbcRuntimeException("K-100204");
        }
        record.setStatus(request.getTradeStatus());
        record.setCallbackTime(record.getCallbackTime() == null ? LocalDateTime.now() : record.getCallbackTime());
        record.setPracticalPrice(request.getAmount());
        record.setFinishTime(request.getFinishTime());
        recordRepository.save(record);
    }

    /**
     * 根据授权码获取微信授权用户openId
     *
     * @param code
     */
    public String getWxOpenIdAndStoreId(String code,Long storeId) {
        List<PayGatewayConfig> configs = gatewayConfigRepository.queryConfigByOpenAndStoreId(storeId);
        Optional<PayGatewayConfig> optional = configs.stream().filter(
                c ->
                        c.getPayGateway().getName() == PayGatewayEnum.WECHAT
                                ||
                                (
                                        c.getPayGateway().getType()
                                                &&
                                                c.getPayGateway().getPayChannelItemList().stream().anyMatch(
                                                        i -> "WeChat".equals(i.getChannel())
                                                                &&
                                                                i.getIsOpen() == IsOpen.YES
                                                )
                                )

        ).findFirst();
        return optional.map(config -> proxy.getWxOpenId(config, code)).orElse(null);
    }

    /**
     * 银联企业支付
     *
     * @param unionPay
     * @return
     */
    public String unionB2BPay(UnionPayRequest unionPay) {
        //是否重复支付
        PayTradeRecord record = recordRepository.findByBusinessId(unionPay.getBusinessId());
        String html = "";
        if (!Objects.isNull(record) && record.getStatus() == TradeStatus.SUCCEED) {
            //如果重复支付，判断状态，已成功状态则做异常提示
            throw new SbcRuntimeException("K-100203");
        } else {
            if (record == null) {
                record = new PayTradeRecord();
                record.setId(GeneratorUtils.generatePT());
            }
            record.setApplyPrice(unionPay.getAmount());
            record.setBusinessId(unionPay.getBusinessId());
            record.setClientIp(unionPay.getClientIp());
            record.setChannelItemId(unionPay.getChannelItemId());
            record.setTradeType(TradeType.PAY);
            record.setCreateTime(LocalDateTime.now());
            record.setStatus(TradeStatus.PROCESSING);
            recordRepository.save(record);
            html = createUnionHtml(unionPay);
        }
        return html;
    }

    /**
     * 银联企业支付同步回调 添加 数据
     *
     * @param resMap
     */
    public void unionCallBack(Map<String, String> resMap, PayTradeRecord record) {
        record.setTradeNo(resMap.get("queryId"));
        if ("00".equals(resMap.get("respCode"))) {
            record.setStatus(TradeStatus.SUCCEED);
        } else {
            record.setStatus(TradeStatus.FAILURE);
        }
        record.setCallbackTime(record.getCallbackTime() == null ? LocalDateTime.now() : record.getCallbackTime());
        record.setPracticalPrice(new BigDecimal(Long.parseLong(resMap.get("txnAmt")) / 100));
        record.setFinishTime(LocalDateTime.now());
        recordRepository.save(record);
    }

    /**
     * 微信支付同步回调 添加 数据
     *
     * @param request
     * @param record
     */
    public void wxPayCallBack(PayTradeRecordRequest request, PayTradeRecord record) {
        if (request.getResult_code().equals(WXPayConstants.SUCCESS)) {
            record.setStatus(TradeStatus.SUCCEED);
        } else {
            record.setStatus(TradeStatus.FAILURE);
        }
        record.setCallbackTime(record.getCallbackTime() == null ? LocalDateTime.now() : record.getCallbackTime());
        record.setPracticalPrice(request.getPracticalPrice());
        record.setFinishTime(LocalDateTime.now());
        record.setTradeNo(request.getTradeNo());
        if(Objects.isNull(record.getApplyPrice())){
            record.setApplyPrice(BigDecimal.ZERO);
        }
        if(Objects.isNull(record.getChannelItemId())){
            record.setChannelItemId(request.getChannelItemId());
        }
        recordRepository.save(record);
    }

    /**
     * 添加交易记录
     *
     * @param recordRequest
     */
    @Transactional
    public void addPayTradeRecord(PayTradeRecordRequest recordRequest) {
        PayTradeRecord record = new PayTradeRecord();
        KsBeanUtil.copyPropertiesThird(recordRequest, record);
        record.setId(GeneratorUtils.generatePT());
        if (recordRequest.getResult_code().equals(WXPayConstants.SUCCESS)) {
            record.setStatus(TradeStatus.SUCCEED);
        } else {
            record.setStatus(TradeStatus.FAILURE);
        }
        record.setTradeType(TradeType.PAY);
        record.setFinishTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());
        recordRepository.deleteByBusinessId(recordRequest.getBusinessId());
        recordRepository.save(record);
    }

    /**
     * 银联企业支付参数拼接
     *
     * @param unionPay
     * @return
     */
    private String createUnionHtml(UnionPayRequest unionPay) {


        SDKConfig.getConfig().loadPropertiesFromSrc();
        //前台页面传过来的
        String merId = unionPay.getApiKey();
        BigDecimal txnAmt = unionPay.getAmount();

        Map<String, String> requestData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put("version", SDKConfig.getConfig().getVersion());                  //版本号，全渠道默认值
        requestData.put("encoding", "UTF-8");          //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        requestData.put("txnType", "01");                          //交易类型 ，01：消费
        requestData.put("txnSubType", "01");                          //交易子类型， 01：自助消费
        requestData.put("bizType", "000202");                      //业务类型 000202: B2B
        requestData.put("channelType", "07");                      //渠道类型 固定07

        /***商户接入参数***/
        requestData.put("merId", merId);                              //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put("accessType", "0");                          //接入类型，0：直连商户
        requestData.put("orderId", unionPay.getBusinessId());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        requestData.put("txnTime", getCurrentTime());
        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put("currencyCode", "156");                      //交易币种（境内商户一般是156 人民币）
        requestData.put("txnAmt", txnAmt.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP)
                .toString());                              //交易金额，单位分，不要带小数点

        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
        //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
        //异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        requestData.put("frontUrl", unionPay.getFrontUrl());

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        requestData.put("backUrl", unionPay.getNotifyUrl());

        //实现网银前置的方法：
        //上送issInsCode字段，该字段的值参考《平台接入接口规范-第5部分-附录》（全渠道平台银行名称-简码对照表）2）联系银联业务运营部门开通商户号的网银前置权限
        //requestData.put("issInsCode", "ABC");                 //发卡机构代码

        // 订单超时时间。
        // 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
        // 此时间建议取支付时的北京时间加15分钟。
        // 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
        requestData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 *
                1000));

        // 请求方保留域，
        // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
        // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
        // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
//		requestData.put("reqReserved", "透传信息1|透传信息2|透传信息3");
        // 2. 内容可能出现&={}[]"'符号时：
        // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
        // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
        //    注意控制数据长度，实际传输的数据长度不能超过1024位。
        //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
//		requestData.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> reqData = AcpService.sign(requestData, "UTF-8");  //报文中certId,
        // signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk
        // .properties文件中的acpsdk.frontTransUrl
        String html = AcpService.createAutoFormHtml(requestFrontUrl, reqData, "UTF-8");   //生成自动跳转的Html表单

        log.info("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);
        //将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
        return html;
    }

    private Map<String, String> unionRefund(PayTradeRecord record, PayGatewayConfig gatewayConfig) {

        SDKConfig.getConfig().loadPropertiesFromSrc();
        String origQryId = record.getTradeNo();
        String txnAmt = record.getApplyPrice().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP)
                .toString();
        String encoding = "UTF-8";
        Map<String, String> data = new HashMap<String, String>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", SDKConfig.getConfig().getVersion());                  //版本号，全渠道默认值
        data.put("encoding", encoding);             //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        data.put("txnType", "04");                           //交易类型 04-退货
        data.put("txnSubType", "00");                        //交易子类型  默认00
        data.put("bizType", "000202");                       //业务类型
        data.put("channelType", "07");                       //渠道类型，07-PC，08-手机

        /***商户接入参数***/
        data.put("merId", gatewayConfig.getApiKey());                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改
        data.put("orderId", record.getBusinessId());          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        data.put("txnTime", getCurrentTime());      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）
        data.put("txnAmt", txnAmt);                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
        data.put("backUrl", gatewayConfig.getBossBackUrl() + "/tradeCallback/unionRefundCallBack");
        //后台通知地址，后台通知参数详见open.unionpay
        // .com帮助中心 下载  产品接口规范
        // 网关支付产品接口规范 退货交易 商户通知,
        // 其他说明同消费交易的后台通知

        /***要调通交易以下字段必须修改***/
        data.put("origQryId", origQryId);      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        // 请求方保留域，
        // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
        // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
        // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
//		data.put("reqReserved", "透传信息1|透传信息2|透传信息3");
        // 2. 内容可能出现&={}[]"'符号时：
        // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
        // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
        //    注意控制数据长度，实际传输的数据长度不能超过1024位。
        //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
//		data.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

        Map<String, String> reqData = AcpService.sign(data, encoding);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getBackRequestUrl();                                //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData, url, encoding);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
//        if(!rspData.isEmpty()){
//            return  rspData.get("respCode");
//        }
//        return null;
        return rspData;

    }

    private PayRecordResult queryPayResult(PayTradeRecord record) {
        PayChannelItem item = channelItemRepository.findById(record.getChannelItemId()).get();

        return proxy.queryPayResult(record, item.getGateway());
    }

    private RefundRecordResult queryRefundResult(PayTradeRecord record, String payObjectId) {
        PayChannelItem item = channelItemRepository.findById(record.getChannelItemId()).get();
        PayGateway gateway = item.getGateway();
        return proxy.queryRefundResult(record, payObjectId, gateway);
    }


    private PayChannelItem getPayChannelItem(Long channelItemId,Long storeId) {
        PayChannelItem item = channelItemRepository.findById(channelItemId).get();
        PayValidates.verfiyPayChannelItem(item);
        // 获取网关
        PayGateway gateway = gatewayRepository.queryByNameAndStoreId(item.getGatewayName(),storeId);
        item.setGateway(gateway);
        return item;
    }

    private PayGateway getPayGatewayNew(String appId,Long storeId) {

        List<PayGateway> gateways = gatewayRepository.queryPayGatewayByCondition(appId, storeId);

//        List<PayChannelItem> items = channelItemRepository.findPayChannelItemByAppIdStoreId(appId, storeId);
        if (CollectionUtils.isEmpty(gateways)) {
            //兼容原来的功能

            log.error("PayService getPayChannelItemNew appId:{} storeId:{} 对应的支付渠道为空 ", appId, storeId);
            throw new SbcRuntimeException("K-999999", "appid和storeId对应的支付渠道信息不存在");
        }
        PayGateway payGateway = gateways.get(0);
//        // 获取网关
//        PayGateway gateway = gatewayRepository.queryByNameAndStoreId(payChannelItem.getGatewayName(),storeId);
//        item.setGateway(gateway);
        if (payGateway.getConfig() == null || StringUtils.isBlank(payGateway.getConfig().getAppId())) {
            log.error("PayService getPayChannelItemNew appId:{} storeId:{} 配置信息有误 payGateway:{}", appId, storeId, JSON.toJSONString(payGateway));
            throw new SbcRuntimeException("K-999999", "appid和storeId对应的配置信息有误");
        }
        return payGateway;
    }



    private void savePayRecord(PayRequest request, PayResult result) {
        PayTradeRecord record = new PayTradeRecord();
        record.setId(GeneratorUtils.generatePT());
        record.setApplyPrice(request.getAmount());
        record.setBusinessId(request.getBusinessId());
        record.setChargeId(result.getObjectId());
        record.setClientIp(request.getClientIp());
        record.setChannelItemId(request.getChannelItemId());
        record.setTradeNo(result.getTradeNo());
        record.setTradeType(TradeType.PAY);
        record.setCreateTime(result.getCreateTime());
        record.setStatus(TradeStatus.PROCESSING);
        recordRepository.deleteByBusinessId(request.getBusinessId());
        recordRepository.save(record);
    }

    private PayTradeRecord saveRefundRecord(RefundRequest request, Long channelItemId, String appId) {
        PayTradeRecord record = new PayTradeRecord();
        record.setId(GeneratorUtils.generatePT());
        record.setApplyPrice(request.getAmount());
        record.setBusinessId(request.getRefundBusinessId());
        record.setClientIp(request.getClientIp());
        record.setChannelItemId(channelItemId);
        record.setTradeType(TradeType.REFUND);
        record.setStatus(TradeStatus.PROCESSING);
        record.setCreateTime(LocalDateTime.now());
        record.setAppId(appId);
        //删除失败或未成功获取到退款对象的记录
        recordRepository.deleteByBusinessId(request.getRefundBusinessId());
        record = recordRepository.save(record);
        return record;
    }

    // 商户发送交易时间 格式:YYYYMMDDhhmmss
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    // AN8..40 商户订单号，不能含"-"或"_"
    public static String getOrderId() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }


    public List<PayChannelItemVO> getAllChannelItem() {
        List<PayChannelItem> list = this.channelItemRepository.findByGatewayName(PayGatewayEnum.WECHAT);
        return KsBeanUtil.convertList(list,PayChannelItemVO.class);
    }
}

