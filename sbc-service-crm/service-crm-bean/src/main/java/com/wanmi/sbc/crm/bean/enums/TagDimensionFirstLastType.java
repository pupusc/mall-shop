package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public enum TagDimensionFirstLastType {

    @ApiEnumProperty("0: 非首末")
    NO_FIRST_LAST,

    @ApiEnumProperty("1：首次")
    FIRST,

    @ApiEnumProperty("2：末次")
    LAST;


    @JsonCreator
    public TagDimensionFirstLastType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
