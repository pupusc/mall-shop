package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum PublishState {

    @ApiEnumProperty("0:未启用")
    NOT_ENABLE,
    @ApiEnumProperty("1:启用")
    ENABLE;

    @JsonCreator
    public static PublishState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
