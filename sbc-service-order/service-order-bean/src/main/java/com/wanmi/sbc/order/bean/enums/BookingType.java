package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum BookingType {


    @ApiEnumProperty("0:全款预售")
    FULL_MONEY,

    @ApiEnumProperty("1:定金预售")
    EARNEST_MONEY;


    @JsonCreator
    public static BookingType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
