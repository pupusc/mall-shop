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

    @ApiEnumProperty("0:tab")
    ONE,
    @ApiEnumProperty("1:关键词")
    TWO,
    @ApiEnumProperty("2:规则")
    THREE,
    @ApiEnumProperty("3:投放内容")
    FOUR;

    @JsonCreator
    public MixedComponentLevel fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public Integer toValue() {
        return this.ordinal();
    }
}
