package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum  PaidCardRuleTypeEnum {

    @ApiEnumProperty("0:付费规则")
    PAY,
    @ApiEnumProperty("1:续费规则")
    REPAY;

    @JsonCreator
    public static PaidCardRuleTypeEnum fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
