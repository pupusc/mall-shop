package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum PrintSize {

    @ApiEnumProperty("打印尺寸58")
    FIFTY_EIGHT,
    @ApiEnumProperty("打印尺寸80")
    EIGHTY;

    @JsonCreator
    public PrintSize fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}