package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @author liutao
 * 评分周期 0：30天，1：90天，2：180天
 * @date 2019/2/27 5:05 PM
 */
@ApiEnum
public enum ScoreCycle {
    /**
     * 30天
     */
    @ApiEnumProperty("30天")
    THIRTY,

    /**
     * 90天
     */
    @ApiEnumProperty("90天")
    NINETY,

    /**
     * 180天
     */
    @ApiEnumProperty("180天")
    ONE_HUNDRED_AND_EIGHTY;

    @JsonCreator
    public static ScoreCycle fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
