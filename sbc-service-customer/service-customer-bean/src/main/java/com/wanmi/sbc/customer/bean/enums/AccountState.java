package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * Created by zhangjin on 2017/4/18.
 */
@ApiEnum
public enum AccountState {
    @ApiEnumProperty("启用")
    ENABLE,
    @ApiEnumProperty("禁用")
    DISABLE,
    @ApiEnumProperty("离职")
    DIMISSION;

    @JsonCreator
    public AccountState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}

