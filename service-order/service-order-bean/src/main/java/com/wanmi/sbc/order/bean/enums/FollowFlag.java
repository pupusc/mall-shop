package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 收藏类型
 * 0：所有 1:采购 2:收藏
 * Created by daiyitian on 2017/3/22.
 */
@ApiEnum
public enum FollowFlag {
    @ApiEnumProperty("0: 所有")
    ALL,
    @ApiEnumProperty("1: 采购")
    PURCHASE,
    @ApiEnumProperty("2: 收藏")
    FOLLOW;
    @JsonCreator
    public FollowFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
