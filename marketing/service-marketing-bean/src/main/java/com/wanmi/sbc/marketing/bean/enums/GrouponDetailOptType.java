package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 拼团操作状态
 */

public enum GrouponDetailOptType {

    @ApiEnumProperty("0：普通商品详情页")
    GOODS_DETAIL,

    @ApiEnumProperty("1：拼团商品详情页")
    GROUPON_GOODS_DETAIL,

    @ApiEnumProperty("2：参团商品详情页")
    GROUPON_JOIN,

    @ApiEnumProperty("3：订单提交页")
    GROUPON_SUBMIT,

    @ApiEnumProperty("4：订单支付页")
    GROUPON_PAY,

    @ApiEnumProperty("5：订单支付回调")
    GROUPON_PAY_BACK;

    @JsonCreator
    public GrouponDetailOptType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
