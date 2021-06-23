package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 一个枚举两个含义(兼容业务)
 * 客户类型,0:平台客户/0:店铺关联的客户 ,1:商家客户/1:店铺发展的客户
 */
@ApiEnum
public enum CustomerType {
    @ApiEnumProperty("0:平台客户/0:店铺关联的客户")
    PLATFORM,
    @ApiEnumProperty("1:商家客户/1:店铺发展的客户")
    SUPPLIER;

    @JsonCreator
    public CustomerType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
