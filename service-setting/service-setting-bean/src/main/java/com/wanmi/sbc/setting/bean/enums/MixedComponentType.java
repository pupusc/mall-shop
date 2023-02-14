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
public enum MixedComponentType {

    @ApiEnumProperty("1")
    GOODS,
    @ApiEnumProperty("2")
    LINK,
    @ApiEnumProperty("3")
    VIDEO,
    @ApiEnumProperty("4")
    ADVERTISEMENT,
    @ApiEnumProperty("5")
    SPECIFIEDCONTENT,
    @ApiEnumProperty("6")
    MODULEGOODS;

    @JsonCreator
    public MixedComponentType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public Integer toValue() {
        return this.ordinal();
    }
}
