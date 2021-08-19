package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>地址层级</p>
 * 0-省级;1-市级;2-区县级;3-乡镇或街道级
 * Created by of628-wenzhi on 2017-05-23-下午3:39.
 */
@ApiEnum(dataType = "java.lang.String")
public enum AddrLevel {

    @ApiEnumProperty("0：省级")
    PROVINCE,
    @ApiEnumProperty("1：市级")
    CITY,
    @ApiEnumProperty("2：区县级")
    DISTRICT,
    @ApiEnumProperty("3：乡镇或街道级")
    STREET;

    @JsonCreator
    public AddrLevel fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
