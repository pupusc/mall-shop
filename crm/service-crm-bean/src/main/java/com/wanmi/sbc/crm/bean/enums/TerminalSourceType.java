package com.wanmi.sbc.crm.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public enum TerminalSourceType {

    @ApiEnumProperty("微信小程序")
    WEIXIN("微信小程序"),

    @ApiEnumProperty("APP")
    APP("APP"),

    @ApiEnumProperty("PC")
    PC("PC"),

    @ApiEnumProperty("H5")
    H5("H5");

    private final String value;

    TerminalSourceType(String value){
        this.value = value;
    }

    public String toValue() {
        return this.value;
    }
}
