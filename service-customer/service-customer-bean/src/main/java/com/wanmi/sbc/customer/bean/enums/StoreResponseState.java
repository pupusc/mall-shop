package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 响应类店铺状态 0、正常 1、关店 2、过期 3、不存在
 * Created by Daiyitian on 2017/11/2.
 */
@ApiEnum
public enum StoreResponseState {
    @ApiEnumProperty("0、正常")
    OPENING,
    @ApiEnumProperty("1、关店")
    CLOSED,
    @ApiEnumProperty("2、过期")
    EXPIRE,
    @ApiEnumProperty("3、不存在")
    NONEXISTENT;

    @JsonCreator
    public StoreResponseState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
