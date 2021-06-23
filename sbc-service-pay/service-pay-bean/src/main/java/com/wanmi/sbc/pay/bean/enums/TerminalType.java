package com.wanmi.sbc.pay.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 终端: 0 pc 1 h5 2 app
 * Created by sunkun on 2017/8/3.
 */
@ApiEnum
public enum TerminalType {

    @ApiEnumProperty("PC")
    PC,

    @ApiEnumProperty("H5")
    H5,

    @ApiEnumProperty("APP")
    APP;

    @JsonCreator
    public TerminalType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
