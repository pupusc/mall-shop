package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 0未删除 1已删除
 * Created by zhangjin on 2017/3/22.
 */
@ApiEnum
public enum DeleteFlag {
    @ApiEnumProperty("0:否")
    NO,
    @ApiEnumProperty("1:是")
    YES;

    @JsonCreator
    public static DeleteFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
