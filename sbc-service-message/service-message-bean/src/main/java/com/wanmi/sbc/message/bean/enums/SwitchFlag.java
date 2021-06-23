package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 节点开关
 */
@ApiEnum
public enum SwitchFlag {

    @ApiEnumProperty(" 0：未启用")
    CLOSE,

    @ApiEnumProperty("1：启用")
    OPEN;
    @JsonCreator
    public static SwitchFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
