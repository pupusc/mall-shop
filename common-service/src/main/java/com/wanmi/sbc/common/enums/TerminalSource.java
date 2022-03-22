package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 终端: 0 pc 1 h5 2 app
 * Created by ruilinxin
 */
@AllArgsConstructor
@Getter
public enum TerminalSource {


    H5(1, "H5", "H5"),
    MINIPROGRAM(2, "MINIPROGRAM", "小程序"),
    MALL_NORMAL(3, "NORMAL", "商城-普通分类"),
    FDDS_DELIVER(4, "FDDS_DELIVER", "樊登读书-实物履约"),
    SUPPLIER(10, "SUPPLIER", "商家后台"),
    PC(11, "PC", "PC"),
    APP(12, "APP", "APP");

    private Integer code;
    private String message;
    private String desc;

    @JsonCreator
    public static TerminalSource fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


    public static TerminalSource getTerminalSource(String name) {
        for (TerminalSource terminal : values()) {
            if (terminal.getMessage().equals(name)) {
                return terminal;
            }
        }
        return TerminalSource.H5;
    }
}
