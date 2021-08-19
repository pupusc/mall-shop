package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum BgTypeEnum {

    //背景类型0背景色；1背景图片
    @ApiEnumProperty("0:背景色")
    COLOR,
    @ApiEnumProperty("1:背景图片")
    IMAGE;

    @JsonCreator
    public static BgTypeEnum fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
