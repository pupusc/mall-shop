package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

public enum SendStatus {
    @ApiEnumProperty(" 0：未开始")
    NO_BEGIN,

    @ApiEnumProperty(" 1：进行中")
    BEGIN,

    @ApiEnumProperty(" 2：结束")
    END,

    @ApiEnumProperty(" 3：失败")
    FAILED;

    @JsonCreator
    public SendStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
