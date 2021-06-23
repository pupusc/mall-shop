package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @ClassName PayCallBackType
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/7/1 17:57
 **/
@ApiEnum
public enum PayCallBackType {

    @ApiEnumProperty("0: 微信支付")
    WECAHT,

    @ApiEnumProperty("1: 支付宝支付")
    ALI,

    @ApiEnumProperty("2: 银联支付")
    UNIONPAY;

    @JsonCreator
    public PayCallBackType fromValue(int value){
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
