package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 入账类型（0：现金 1：优惠券）
 */
@ApiEnum
public enum RewardFlag {
    @ApiEnumProperty("入账类型 0:现金")
    CASH,
    @ApiEnumProperty("入账类型 1:优惠券")
    COUPON;

    @JsonCreator
    public RewardFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
