package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 微信提现 微信openId来源 0:PC 1:H5 2:App
 * @author chenli
 */
@ApiEnum(dataType = "java.lang.String")
public enum DrawCashSource {

    /**
     * PC
     */
    @ApiEnumProperty("PC")
    PC("PC"),

    /**
     * MOBILE
     */
    @ApiEnumProperty("MOBILE")
    MOBILE("MOBILE"),

    /**
     * APP
     */
    @ApiEnumProperty("APP")
    APP("APP"),

    /**
     * MINI
     */
    @ApiEnumProperty("MINI")
    MINI("MINI");


    private String desc;

    DrawCashSource(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static DrawCashSource fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
