package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 配送周期 0:每日一期 1:每周一期 2:每月一期
 */
@ApiEnum
public enum DeliveryCycle {

    @ApiEnumProperty("0: 每日一期")
    EVERYDAY,

    @ApiEnumProperty("0: 每周一期")
    WEEKLY,

    @ApiEnumProperty("2: 每月一期")
    MONTHLY;

    @JsonCreator
    public static DeliveryCycle fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
