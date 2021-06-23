package com.wanmi.sbc.pay.gateway.interfaces;


import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.api.request.PayExtraRequest;
import com.wanmi.sbc.pay.api.request.RefundRequest;
import com.wanmi.sbc.pay.model.entity.PayRecordResult;
import com.wanmi.sbc.pay.model.entity.PayResult;
import com.wanmi.sbc.pay.model.entity.RefundRecordResult;
import com.wanmi.sbc.pay.model.entity.RefundResult;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;

/**
 * <p>网关处理器，包含一组基本的支付网关账务操作接口</p>
 * Created by of628-wenzhi on 2017-08-04-下午5:25.
 */
public interface GatewayHandler {

    /**
     * 获取网关枚举(name)
     *
     * @return key
     */
    PayGatewayEnum getPayGateWayKey();

    /**
     * 支付
     *
     * @param request      请求参数
     * @param channelItem  支付渠道项，包含网关及配置信息
     * @return PayResult   支付结果
     */
    PayResult pay(PayExtraRequest request, PayChannelItem channelItem);

    /**
     * 退款
     *
     * @param request      请求参数
     * @param channelItem  支付渠道项，包含网关及配置信息a
     * @return
     */
    RefundResult refund(RefundRequest request, PayChannelItem channelItem);

    /**
     * 查询支付信息
     *
     * @param record 交易记录，接口会将查询后的结果填充到记录中返回
     * @param config 包含appId,apiKey,privateKey等网关配置信息
     * @return
     */
    PayRecordResult queryPayResult(PayTradeRecord record, PayGatewayConfig config);

    /**
     * 查询退款信息
     *
     * @param record  交易记录，接口会将查询后的结果填充到记录中返回
     * @param payObjectId 与退款关联的支付对象id
     * @param config  包含appId,apiKey,privateKey等网关配置信息
     * @return
     */
    RefundRecordResult queryRefundResult(PayTradeRecord record, String payObjectId, PayGatewayConfig config);

    /**
     * 获取微信端授权用户的OpenId，非微信支付网关或非包含微信支付渠道的聚合网关返回null
     * @param appId   微信公众号appId
     * @param secret  微信公众号应用密钥
     * @param code    授权码
     * @return
     */
    String getWxOpenId(String appId, String secret, String code);
}
