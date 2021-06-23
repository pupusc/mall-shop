package com.wanmi.sbc.pay.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 微信支付交易类型
 */
@ApiEnum
public enum WxPayTradeType {

    // 统一下单接口:原生扫码支付
    @ApiEnumProperty("NATIVE")
    NATIVE,

    // H5支付的交易类型为MWEB
    @ApiEnumProperty("MWEB")
    MWEB,

    // 统一下单接口:公众号支付
    @ApiEnumProperty("JSAPI")
    JSAPI,

    // 统一下单接口:app支付
    @ApiEnumProperty("APP")
    APP;

    @JsonCreator
    public WxPayTradeType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
