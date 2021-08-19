package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 限售周期： 0-周  1-月  2-年
 * Created by Daiyitian on 2017/4/13.
 */
@ApiEnum
public enum PersonRestrictedCycle {

    @ApiEnumProperty("0：日")
    RESTRICTED_EVERY_DAY,

    @ApiEnumProperty("1：周")
    RESTRICTED_EVERY_WEEK,

    @ApiEnumProperty("2：月")
    RESTRICTED_EVERY_MONTH,

    @ApiEnumProperty("3：年")
    RESTRICTED_EVERY_YEAR;

    @JsonCreator
    public PersonRestrictedCycle fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
