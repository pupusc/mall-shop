package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 特定会员限售方式 : 0-会员等级  1-指定会员
 * Created by Daiyitian on 2017/4/13.
 */
@ApiEnum
public enum AssignPersonRestrictedType {
    @ApiEnumProperty("0：会员等级")
    RESTRICTED_CUSTOMER_LEVEL,

    @ApiEnumProperty("1：指定会员")
    RESTRICTED_ASSIGN_CUSTOMER;

    @JsonCreator
    public AssignPersonRestrictedType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
