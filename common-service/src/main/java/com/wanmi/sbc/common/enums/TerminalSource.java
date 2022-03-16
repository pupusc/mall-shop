package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 终端: 0 pc 1 h5 2 app
 * Created by ruilinxin
 */
@ApiEnum
@AllArgsConstructor
@Getter
public enum TerminalSource {

//    @ApiEnumProperty("代客下单")
    SUPPLIER(4, "SUPPLIER"),
//    @ApiEnumProperty("H5")
    H5(1, "H5"),
//    @ApiEnumProperty("PC")
    PC(5, "PC"),
//    @ApiEnumProperty("APP")
    APP(1, "APP"),
//    @ApiEnumProperty("小程序")
    MINIPROGRAM(2, "MINIPROGRAM"),
//    @ApiEnumProperty("樊登app赠品")
    FD_APP_GIFT(3, "FD_APP_GIFT");

    private Integer code;
    private String message;

    @JsonCreator
    public static TerminalSource fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


    public static TerminalSource getTerminalSource(String name) {
//        switch (name) {
//            case "PC":
//                return TerminalSource.PC;
//            case "H5":
//                return TerminalSource.H5;
//            case "APP":
//                return TerminalSource.APP;
//            case "MINIPROGRAM":
//                return TerminalSource.MINIPROGRAM;
//            case "SUPPLIER":
//                return TerminalSource.SUPPLIER;
//            default:
//                throw new SbcRuntimeException("参数错误！请检查TerminalType是否正确！！");
//        }
        for (TerminalSource terminal : values()) {
            if (terminal.getMessage().equals(name)) {
                return terminal;
            }
        }
        throw new SbcRuntimeException("参数错误！请检查TerminalType是否正确！！");
    }

//    public static TerminalSource getSource(String name) {
//        for (TerminalSource terminal : values()) {
//            if (terminal.getMessage().equals(name)) {
//                return terminal;
//            }
//        }
//        throw new SbcRuntimeException("参数错误！请检查TerminalType是否正确！！");
//    }
}
