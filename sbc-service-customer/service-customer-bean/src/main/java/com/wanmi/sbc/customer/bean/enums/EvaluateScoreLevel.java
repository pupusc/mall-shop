package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @author liutao
 * 评分星数 0 1 2 3 4 5
 * @date 2019/2/27 5:05 PM
 */
@ApiEnum
public enum EvaluateScoreLevel {
    /**
     * 0星
     */
    @ApiEnumProperty("0星")
    ZERO,

    /**
     * 1星
     */
    @ApiEnumProperty("1星")
    ONE,

    /**
     * 2星
     */
    @ApiEnumProperty("2星")
    TWO,

    /**
     * 3星
     */
    @ApiEnumProperty("3星")
    THREE,

    /**
     * 4星
     */
    @ApiEnumProperty("4星")
    FOUR,

    /**
     * 5星
     */
    @ApiEnumProperty("5星")
    FIVE;


    @JsonCreator
    public static EvaluateScoreLevel fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
