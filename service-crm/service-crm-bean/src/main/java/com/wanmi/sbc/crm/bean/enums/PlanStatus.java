package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

public enum PlanStatus {
    @ApiEnumProperty(" 0：未开始")
    NO_BEGIN,

    @ApiEnumProperty(" 1：进行中")
    BEGIN,

    @ApiEnumProperty(" 2：暂停中")
    STOP,

    @ApiEnumProperty(" 3：已结束")
    END;

    @JsonCreator
    public PlanStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
