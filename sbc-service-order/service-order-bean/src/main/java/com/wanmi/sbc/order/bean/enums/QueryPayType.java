package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 筛选支付方式
 * Created by dyt on 2020/6/16.
 */
@ApiEnum
public enum QueryPayType {

    @ApiEnumProperty("线下支付")
    OFFLINE,

    @ApiEnumProperty("在线支付")
    ONLINE,

    @ApiEnumProperty("积余额支付")
    BALANCE,

    @ApiEnumProperty("纯积分支付")
    POINT,

    @ApiEnumProperty("积分+在线支付")
    POINT_ONLINE,

    @ApiEnumProperty("积分+余额支付")
    POINT_BALANCE;

    @JsonCreator
    public QueryPayType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
