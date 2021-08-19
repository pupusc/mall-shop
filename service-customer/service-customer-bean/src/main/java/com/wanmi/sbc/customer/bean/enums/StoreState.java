package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 店铺状态 0、开启 1、关店
 * Created by CHENLI on 2017/11/2.
 */
@ApiEnum
public enum StoreState {

    @ApiEnumProperty("0、开启")
    OPENING,
    @ApiEnumProperty("1、关店")
    CLOSED;

    @JsonCreator
    public StoreState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
