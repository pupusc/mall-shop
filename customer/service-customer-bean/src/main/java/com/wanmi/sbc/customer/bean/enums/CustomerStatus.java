package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 账号状态 0：启用中  1：禁用中
 * Created by CHENLI on 2017/4/13.
 */
@ApiEnum
public enum CustomerStatus {
    @ApiEnumProperty("0：启用中")
    ENABLE,
    @ApiEnumProperty("1：禁用中")
    DISABLE;

    @JsonCreator
    public CustomerStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
