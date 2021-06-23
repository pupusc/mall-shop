package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum EffectStatusEnum {

    @ApiEnumProperty("0:过期")
    INVALID,
    @ApiEnumProperty("1:生效")
    EFFECT,

    @ApiEnumProperty("2:未生效")
    NOEFFECT,
    @ApiEnumProperty("3:已关闭")
    CLOSED;

    @JsonCreator
    public static EffectStatusEnum fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
