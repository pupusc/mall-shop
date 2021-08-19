package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @author: lq
 * @CreateTime:2019-02-11 16:08
 * @Description:todo
 */
@ApiEnum
public enum ImageShowType {
    @ApiEnumProperty("0:小图")
    SMALL,
    @ApiEnumProperty("1:大图")
    BIG;

    @JsonCreator
    public ImageShowType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
