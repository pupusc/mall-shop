package com.wanmi.sbc.pay.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>支付网关枚举</p>
 * Created by of628-wenzhi on 2017-08-04-下午10:25.
 */
@ApiEnum
public enum PayGatewayEnum {
    /**
     * 银联
     */
    @ApiEnumProperty("银联")
    UNIONPAY,

    /**
     * 微信
     */
    @ApiEnumProperty("微信")
    WECHAT,

    /**
     * 支付宝
     */
    @ApiEnumProperty("支付宝")
    ALIPAY,

    /**
     * 银联b2b
     */
    @ApiEnumProperty("银联b2b")
    UNIONB2B,

    /**
     * 拼++
     */
    @ApiEnumProperty("拼++")
    PING,

    /**
     * 余额支付
     */
    @ApiEnumProperty("余额支付")
    BALANCE;

    @JsonCreator
    public PayGatewayEnum fromValue(String name) {
        return PayGatewayEnum.valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
