package com.wanmi.sbc.pay.gateway;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.pay.api.request.PayExtraRequest;
import com.wanmi.sbc.pay.api.request.RefundRequest;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.gateway.interfaces.GatewayHandler;
import com.wanmi.sbc.pay.model.entity.PayRecordResult;
import com.wanmi.sbc.pay.model.entity.PayResult;
import com.wanmi.sbc.pay.model.entity.RefundRecordResult;
import com.wanmi.sbc.pay.model.entity.RefundResult;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGateway;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>交易网关业务代理，主要负责网关路由与操作派发</p>
 * Created by of628-wenzhi on 2017-08-04-下午3:49.
 */
@Component
public final class GatewayProxy implements InitializingBean {

    @Resource
    private List<GatewayHandler> gatewayHandlers;

    private Map<PayGatewayEnum, GatewayHandler> gatewayHandlerMap = new HashMap<>();

    /**
     * 支付操作
     *
     * @return 支付结果
     */
    public PayResult pay(PayExtraRequest payRequest, PayChannelItem channelItem) {
        Function<GatewayHandler, PayResult> payCall = (g) -> g.pay(payRequest, channelItem);
        return execute(payCall, channelItem.getGateway());
    }

    /**
     * 退款操作
     *
     * @return 退款结果
     */
    public RefundResult refund(RefundRequest refundRequest, PayChannelItem channelItem) {
        Function<GatewayHandler, RefundResult> refundCall = (g) -> g.refund(refundRequest, channelItem);
        return execute(refundCall, channelItem.getGateway());
    }

    /**
     * 查询渠道支付结果
     *
     * @return 支付结果
     */
    public PayRecordResult queryPayResult(PayTradeRecord record, PayGateway payGateway) {
        Function<GatewayHandler, PayRecordResult> queryCall = (g) -> g.queryPayResult(record, payGateway.getConfig());
        return execute(queryCall, payGateway);
    }

    /**
     * 查询渠道退款结果并填充
     *
     * @return 支付结果
     */
    public RefundRecordResult queryRefundResult(PayTradeRecord record, String payObjectId, PayGateway payGateway) {
        Function<GatewayHandler, RefundRecordResult> queryCall = (g) -> g.queryRefundResult(record, payObjectId, payGateway.getConfig());
        return execute(queryCall, payGateway);
    }

    /**
     * 向包含微信支付渠道的网关查询微信授权用户的openId
     *
     * @return 微信openId
     */
    public String getWxOpenId(PayGatewayConfig config, String code) {
        PayGateway gateway = config.getPayGateway();
        String appId = gateway.getType() ? config.getAppId2() : config.getAppId();
        Function<GatewayHandler, String> queryCall = (g) -> g.getWxOpenId(appId, config.getSecret(), code);
        return execute(queryCall, config.getPayGateway());
    }


    private <R> R execute(Function<GatewayHandler, R> function, PayGateway payGateway) {
        Optional<GatewayHandler> optional = getHandler(payGateway.getName());
        return optional.map(function).orElseThrow(() -> new SbcRuntimeException("K-100205"));
    }

    private Optional<GatewayHandler> getHandler(PayGatewayEnum gatewayName) {
        return Optional.ofNullable(
                this.gatewayHandlerMap.get(gatewayName)
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.gatewayHandlerMap = this.gatewayHandlers.stream().collect(
                Collectors.toMap(GatewayHandler::getPayGateWayKey, Function.identity())
        );
    }

}
