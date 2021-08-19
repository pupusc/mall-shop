package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 生效终端 0全部,1.PC,2.移动端,3.APP
 */
@ApiEnum
public enum TerminalType {

    @ApiEnumProperty("0：全部")
    ALL,

    @ApiEnumProperty("1：PC")
    PC,

    @ApiEnumProperty("2：移动端H5")
    H5,

    @ApiEnumProperty("3：APP")
    APP;

    @JsonCreator
    public static TerminalType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
