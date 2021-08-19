package com.wanmi.sbc.customer.bean.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum AccessType {

    @ApiEnumProperty("0:用户购买")
    BUY,
    @ApiEnumProperty("1:有赞同步")
    SYNC;

    @JsonCreator
    public static AccessType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
