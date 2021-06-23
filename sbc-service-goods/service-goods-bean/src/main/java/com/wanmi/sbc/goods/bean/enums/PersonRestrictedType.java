package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 按人的限售方式 : 0-终生限售  1-周期限售
 * Created by Daiyitian on 2017/4/13.
 */
@ApiEnum
public enum PersonRestrictedType {
    @ApiEnumProperty("0：终生限售")
    RESTRICTED_ALL_LIFE,

    @ApiEnumProperty("1：周期限售")
    RESTRICTED_BY_CYCLE;

    @JsonCreator
    public PersonRestrictedType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
