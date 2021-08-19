package com.wanmi.sbc.pay.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 0关闭 1开启
 * Created by sunkun on 2017/8/3.
 */
@ApiEnum
public enum IsOpen {

    @ApiEnumProperty("关闭")
    NO,

    @ApiEnumProperty("开启")
    YES;

    @JsonCreator
    public IsOpen fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
