package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum PayFlagEnum {
    //背景类型0背景色；1背景图片
    @ApiEnumProperty("0:0元支付，无需跳转收银台")
    NO_PAY,
    @ApiEnumProperty("1:需要跳转收银台")
    NEED_PAY;

    @JsonCreator
    public static PayFlagEnum fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
