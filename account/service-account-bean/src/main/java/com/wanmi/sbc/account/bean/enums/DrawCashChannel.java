package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 提现渠道
 * @author chenyufei
 */
@ApiEnum(dataType = "java.lang.String")
public enum DrawCashChannel {

    /**
     * 微信
     */
    @ApiEnumProperty("微信")
    WECHAT("微信"),

    /**
     * 支付宝
     */
    @ApiEnumProperty("支付宝")
    ALPAY("支付宝");


    private String desc;

    DrawCashChannel(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static DrawCashChannel fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
