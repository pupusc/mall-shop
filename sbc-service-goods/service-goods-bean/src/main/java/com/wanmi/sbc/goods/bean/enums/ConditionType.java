package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 包邮条件类别(0:件/重/体积计价方式,1:金额,2:计价方式+金额)
 * Created by sunkun on 2018/5/4.
 */
@ApiEnum
public enum ConditionType {

    @ApiEnumProperty("0:件/重/体积计价方式")
    VALUATION,

    @ApiEnumProperty("1:金额")
    MONEY,

    @ApiEnumProperty("2:计价方式+金额")
    VALUATIONANDMONEY;

    @JsonCreator
    public ConditionType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
