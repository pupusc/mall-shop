package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Description 层级
 * @Author zh
 * @Date  2023/2/10 15:05
 */
@ApiEnum(dataType = "java.lang.String")
public enum MixedComponentLevel {

    @ApiEnumProperty("0")
    ONE,
    @ApiEnumProperty("1")
    TWO,
    @ApiEnumProperty("2")
    THREE;

    @JsonCreator
    public MixedComponentLevel fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public Integer toValue() {
        return this.ordinal();
    }
}
