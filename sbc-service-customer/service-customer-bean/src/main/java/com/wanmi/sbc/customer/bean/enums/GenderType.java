package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 性别，0：保密，1：男，2：女
 * @author xuyunpeng
 */
@ApiEnum
public enum GenderType {

    @ApiEnumProperty("保密")
    SECRET,
    @ApiEnumProperty("男")
    MALE,
    @ApiEnumProperty("女")
    FEMALE;

    @JsonCreator
    public GenderType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
