package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 配送方案 0:商家主导配送 1:客户主导配送
 */
@ApiEnum
public enum DeliveryPlan {
    @ApiEnumProperty("0: 商家主导配送")
    BUSINESS,

    @ApiEnumProperty("1: 客户主导配送")
    CUSTOMER;

    @JsonCreator
    public static DeliveryPlan fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
