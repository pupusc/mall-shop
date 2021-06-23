package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum TimeUnitEnum {

    //时间单位：0天，1月，2年

    @ApiEnumProperty("0:天")
    DAY,
    @ApiEnumProperty("1:月")
    MONTH,
    @ApiEnumProperty("1:年")
    YEAR;

    @JsonCreator
    public static TimeUnitEnum fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
