package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 *  商家类型 0、平台自营 1、第三方商家
 */
@ApiEnum
public enum CompanyType {
    @ApiEnumProperty("0:平台自营")
    PLATFORM,
    @ApiEnumProperty("1:第三方商家")
    SUPPLIER;

    @JsonCreator
    public CompanyType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
