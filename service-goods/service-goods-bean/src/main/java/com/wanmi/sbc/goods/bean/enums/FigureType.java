package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 商详作家类型
 * 1作家2翻译家
 */
@ApiEnum
public enum FigureType {

    @ApiEnumProperty("0: 未上架")
    WRITER,
    @ApiEnumProperty("1: 已上架")
    TRANSLATOR;

    @JsonCreator
    public static FigureType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public String toValue() {
        return String.valueOf(this.ordinal() + 1);
    }
}
