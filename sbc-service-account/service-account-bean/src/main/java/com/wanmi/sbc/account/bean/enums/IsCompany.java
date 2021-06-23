package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 是否是企业
 * Created by zhangjin on 2017/5/7.
 */
@ApiEnum
public enum IsCompany {

    @ApiEnumProperty("是")
    YES,

    @ApiEnumProperty("否")
    NO;

    @JsonCreator
    public static IsCompany fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
