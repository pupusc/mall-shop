package com.wanmi.sbc.pay.gateway.handler;

import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;
import com.pingplusplus.util.WxpubOAuth;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.api.request.PayExtraRequest;
import com.wanmi.sbc.pay.api.request.RefundRequest;
import com.wanmi.sbc.pay.gateway.interfaces.GatewayHandler;
import com.wanmi.sbc.pay.model.entity.PayRecordResult;
import com.wanmi.sbc.pay.model.entity.PayResult;
import com.wanmi.sbc.pay.model.entity.RefundRecordResult;
import com.wanmi.sbc.pay.model.entity.RefundResult;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import com.wanmi.sbc.pay.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Ping++交易处理器</p>
 * Created by of628-wenzhi on 2017-08-04-下午2:38.
 */
@Component
@Slf4j
public class PingHandler implements GatewayHandler {

    @Override
    public PayGatewayEnum getPayGateWayKey() {
        return PayGatewayEnum.PING;
    }

    @Override
    public PayResult pay(PayExtraRequest request, PayChannelItem channelItem) {
        Map<String, Object> paramMap = buildPayParamMap(request, channelItem);
        try {
            Pingpp.apiKey = channelItem.getGateway().getConfig().getApiKey();
            Pingpp.privateKey = channelItem.getGateway().getConfig().getPrivateKey();
            Charge charge = Charge.create(paramMap);
            if (StringUtils.isNotBlank(charge.getFailureCode())) {
                log.error("Create payment object from payment gateway [Ping] a has error information," +
                                "errorCode={}," +
                                "errorMsg={}," +
                                "charge={}," +
                                "request={}," +
                                "channelItem={}",
                        charge.getFailureCode(),
                        charge.getFailureMsg(),
                        charge,
                        request,
                        channelItem
                );
                throw new SbcRuntimeException("K-000001");
            }
            return PayResult.builder()
                    .createTime(CommonUtils.getLocalDateTimeFromUnixTimestamp(charge.getCreated()))
                    .data(charge)
                    .objectId(charge.getId())
                    .tradeNo(paramMap.get("order_no").toString())
                    .build();

        } catch (Exception e) {
            log.error("Create payment object from payment gateway [Ping] error," +
                            "request={}," +
                            "channelItem={}",
                    request,
                    channelItem,
                    e
            );
            throw new SbcRuntimeException("K-000001");
        }
    }

    @Override
    public RefundResult refund(RefundRequest request, PayChannelItem channelItem) {
        Map<String, Object> paramMap = buildRefundParamMap(request, channelItem);
        try {
            Pingpp.apiKey = channelItem.getGateway().getConfig().getApiKey();
            Pingpp.privateKey = channelItem.getGateway().getConfig().getPrivateKey();
            Refund refund = Refund.create(request.getPayObjectId(), paramMap);
            if ("failed".equals(refund.getStatus())) {
                log.error("Execute the refund operation from payment gateway [Ping] a has error information," +
                                "errorCode={}," +
                                "errorMsg={}," +
                                "refund={}," +
                                "request={}," +
                                "channelItem={}",
                        refund.getFailureCode(),
                        refund.getFailureMsg(),
                        refund,
                        request,
                        channelItem
                );
                throw new SbcRuntimeException("K-000001");
            }
            return RefundResult.builder()
                    .createTime(CommonUtils.getLocalDateTimeFromUnixTimestamp(refund.getCreated()))
                    .data(refund)
                    .payObjectId(refund.getCharge())
                    .refundObjectId(refund.getId())
                    .tradeNo(refund.getTransactionNo())
                    .build();
        } catch (Exception e) {
            log.error("Execute the refund operation from payment gateway [Ping] error," +
                            "request={}," +
                            "channelItem={}",
                    request,
                    channelItem,
                    e
            );
            throw new SbcRuntimeException("K-000001");
        }
    }

    @Override
    public PayRecordResult queryPayResult(PayTradeRecord record, PayGatewayConfig config) {
        PayRecordResult result = new PayRecordResult();
        result.setRecord(record);
        try {
            Pingpp.apiKey = config.getApiKey();
            Charge charge = Charge.retrieve(record.getChargeId());
            record.setPracticalPrice(charge.getPaid() ? new BigDecimal(charge.getAmount() / 100) : null);
            record.setStatus(charge.getPaid() ? TradeStatus.SUCCEED : TradeStatus.PROCESSING);
            record.setFinishTime(charge.getPaid() ? CommonUtils.getLocalDateTimeFromUnixTimestamp(charge.getTimePaid()) : null);
            result.setTimeExpire(CommonUtils.getLocalDateTimeFromUnixTimestamp(charge.getTimeExpire()));
            result.setObject(charge);
            if (StringUtils.isNotBlank(charge.getFailureCode())) {
                log.error("Query payment object from payment gateway [Ping] a has error information," +
                                "errorCode={}," +
                                "errorMsg={}," +
                                "charge={}",
                        charge.getFailureCode(),
                        charge.getFailureMsg(),
                        charge
                );
            }
        } catch (Exception e) {
            log.error("An exception occurs in the way the payment gateway is executed [Ping.Charge.retrieve]," +
                            "record={}," +
                            "payGatewayConfig={}",
                    record,
                    config,
                    e
            );
        }
        return result;
    }

    @Override
    public RefundRecordResult queryRefundResult(PayTradeRecord record, String payObjectId, PayGatewayConfig config) {
        RefundRecordResult result = new RefundRecordResult();
        result.setRecord(record);
        try {
            Pingpp.apiKey = config.getApiKey();
            Refund refund = Refund.retrieve(payObjectId, record.getChargeId());
            record.setPracticalPrice(refund.getSucceed() ? new BigDecimal(refund.getAmount() / 100) : null);
            record.setStatus(refund.getSucceed() ? TradeStatus.SUCCEED
                    : "failed".equals(refund.getStatus()) ? TradeStatus.FAILURE : TradeStatus.PROCESSING);
            record.setFinishTime(refund.getSucceed() ? CommonUtils.getLocalDateTimeFromUnixTimestamp(refund.getTimeSucceed()) : null);
            result.setObject(refund);
            if (StringUtils.isNotBlank(refund.getFailureCode())) {
                log.error("Query refund object from payment gateway [Ping] a has error information," +
                                "errorCode={}," +
                                "errorMsg={}," +
                                "refund={}",
                        refund.getFailureCode(),
                        refund.getFailureMsg(),
                        refund
                );
            }
        } catch (Exception e) {
            log.error("An exception occurs in the way the payment gateway is executed [Ping.Refund.retrieve]," +
                            "record={}," +
                            "payObjectId={}," +
                            "payGatewayConfig={}",
                    record,
                    payObjectId,
                    config,
                    e
            );
        }
        return result;
    }

    @Override
    public String getWxOpenId(String appId, String secret, String code) {
        try {
            return WxpubOAuth.getOpenId(appId, secret, code);
        } catch (Exception e) {
            log.error("An exception occurs in the way the payment gateway is executed [Ping.getOpenId]," +
                            "appId={}," +
                            "secret={}," +
                            "code={}",
                    appId,
                    secret,
                    code,
                    e
            );
            return null;
        }
    }

    private Map<String, Object> buildPayParamMap(PayExtraRequest request, PayChannelItem channelItem) {
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, String> app = new HashMap<>();
        app.put("id", channelItem.getGateway().getConfig().getAppId());
        paramMap.put("order_no", System.currentTimeMillis() + RandomStringUtils.randomNumeric(7));
        paramMap.put("app", app);
        paramMap.put("channel", channelItem.getCode());
        paramMap.put("amount", request.getAmount().multiply(new BigDecimal(100)));
        paramMap.put("client_ip", request.getClientIp());
        paramMap.put("currency", "cny");
        paramMap.put("subject", request.getSubject());
        paramMap.put("body", request.getBody());
        Map<String, Object> extraMap = PingPayExtra.channelExtra(channelItem.getCode(), request);
        if (!Objects.isNull(extraMap)) {
            paramMap.put("extra", extraMap);
        }
        return paramMap;
    }

    private Map<String, Object> buildRefundParamMap(RefundRequest request, PayChannelItem channelItem) {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", request.getAmount().multiply(new BigDecimal(100)));
        map.put("description", request.getDescription());
        return map;
    }

}
