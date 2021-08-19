package com.wanmi.sbc.common.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum(dataType = "java.lang.String")
public enum SpecialSymbols {

    @ApiEnumProperty("&")
    AND("&"),
    @ApiEnumProperty("||")
    OR("||"),
    @ApiEnumProperty(":")
    COLON(":"),
    @ApiEnumProperty("_")
    UNDERLINE("_"),
    @ApiEnumProperty("@")
    AT("@");

    private final String value;

    SpecialSymbols(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }
}