package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * Created by hht on 2017/12/6.
 */
@ApiEnum
public enum StoreType {

    @ApiEnumProperty("0:供应商")
    PROVIDER,

    @ApiEnumProperty("1:商家")
    SUPPLIER;

    @JsonCreator
    public static StoreType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
