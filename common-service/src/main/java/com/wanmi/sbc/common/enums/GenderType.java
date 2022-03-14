package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 性别类型 0女 1男 2保密
 * @author zhangwenchang
 */
@ApiEnum
public enum GenderType {
    @ApiEnumProperty("女")
    FEMALE,
    @ApiEnumProperty("男")
    MALE,
    @ApiEnumProperty("保密")
    SECRET;

    @JsonCreator
    public GenderType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
