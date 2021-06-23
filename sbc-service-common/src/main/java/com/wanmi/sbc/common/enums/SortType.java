package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 排序枚举
 * Created by daiyitian on 2017/4/22.
 */
@ApiEnum
public enum SortType {
    /**
     * asc:升序
     */
    @ApiEnumProperty("asc:升序")
    ASC("asc"),

    /**
     * desc:倒序
     */
    @ApiEnumProperty("desc:倒序")
    DESC("desc");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    @JsonValue
    public String toValue() {
        return value;
    }
}
