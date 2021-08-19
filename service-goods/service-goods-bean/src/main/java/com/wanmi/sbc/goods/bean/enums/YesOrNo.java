package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

public enum YesOrNo {
    @ApiEnumProperty("0:否")
    No,

    @ApiEnumProperty("1:是")
    YES;

    @JsonCreator
    public YesOrNo fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
