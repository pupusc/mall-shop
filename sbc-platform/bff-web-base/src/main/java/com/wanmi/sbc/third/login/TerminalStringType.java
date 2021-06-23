package com.wanmi.sbc.third.login;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 10:13 AM 2018/8/8
 * @Description: 字符串类型终端
 */
@ApiEnum
public enum TerminalStringType {

    @ApiEnumProperty("PC")
    PC,

    @ApiEnumProperty("MOBILE")
    MOBILE,

    @ApiEnumProperty("APP")
    APP,

    @ApiEnumProperty("WEAPP")
    WEAPP;

    @JsonCreator
    public static TerminalStringType fromValue(String name) {
        return valueOf(name.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

}
