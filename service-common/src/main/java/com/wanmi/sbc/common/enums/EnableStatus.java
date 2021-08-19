package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 0未启用 1已启用
 * Created by yinxianzhi on 2019/4/8.
 */
@ApiEnum
public enum EnableStatus {
    @ApiEnumProperty("未启用")
    DISABLE,
    @ApiEnumProperty("已启用")
    ENABLE;

    @JsonCreator
    public static EnableStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
