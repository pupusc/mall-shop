package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * Created by hht on 2017/12/6.
 */
@ApiEnum
public enum ReturnStatus {

    @ApiEnumProperty("已退")
    RETURNED,

    @ApiEnumProperty("进行中")
    RETURNING;

    @JsonCreator
    public static ReturnStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
