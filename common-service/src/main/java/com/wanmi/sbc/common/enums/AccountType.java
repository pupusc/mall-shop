package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 账号类型 0 b2b账号 1 s2b平台端账号 2 s2b商家端账号
 * Created by chenli on 2017/3/22.
 */
@ApiEnum
public enum AccountType {
    @ApiEnumProperty("b2b账号")
    b2bBoss,
    @ApiEnumProperty("s2b平台端账号")
    s2bBoss,
    @ApiEnumProperty("s2b商家端账号")
    s2bSupplier,
    @ApiEnumProperty("s2b供应商端账号")
    s2bProvider;
    @JsonCreator
    public static AccountType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
