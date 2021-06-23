package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import com.wanmi.sbc.common.exception.SbcRuntimeException;

/**
 * 终端: 0 pc 1 h5 2 app
 * Created by ruilinxin
 */
@ApiEnum
public enum TerminalSource {

    @ApiEnumProperty("代客下单")
    SUPPLIER,
    @ApiEnumProperty("H5")
    H5,
    @ApiEnumProperty("PC")
    PC,
    @ApiEnumProperty("APP")
    APP,
    @ApiEnumProperty("小程序")
    MINIPROGRAM;

    @JsonCreator
    public static TerminalSource fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


    public static TerminalSource getTerminalSource(String name) {
        switch (name) {
            case "PC":
                return TerminalSource.PC;
            case "H5":
                return TerminalSource.H5;
            case "APP":
                return TerminalSource.APP;
            case "MINIPROGRAM":
                return TerminalSource.MINIPROGRAM;
            case "SUPPLIER":
                return TerminalSource.SUPPLIER;
            default:
                throw new SbcRuntimeException("参数错误！请检查TerminalType是否正确！！");
        }
    }
}
