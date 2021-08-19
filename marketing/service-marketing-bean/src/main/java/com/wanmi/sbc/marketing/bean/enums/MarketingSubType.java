package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum MarketingSubType {
    /**
     * 满金额减
     */
    @ApiEnumProperty("0：满金额减")
    REDUCTION_FULL_AMOUNT,

    /**
     * 满数量减
     */
    @ApiEnumProperty("1：满数量减")
    REDUCTION_FULL_COUNT,

    /**
     * 满金额折
     */
    @ApiEnumProperty("2：满金额折")
    DISCOUNT_FULL_AMOUNT,

    /**
     * 满数量折
     */
    @ApiEnumProperty("3：满数量折")
    DISCOUNT_FULL_COUNT,

    /**
     * 满金额赠
     */
    @ApiEnumProperty("4：满金额赠")
    GIFT_FULL_AMOUNT,

      /**
     * 满数量赠
     */
    @ApiEnumProperty("5：满数量赠")
    GIFT_FULL_COUNT,

    /**
     * 一口价
     */
    @ApiEnumProperty("6：一口价")
    BUYOUT_PRICE,

    /**
     * 第二件半价优惠活动
     */
    @ApiEnumProperty("7：第二件半价优惠活动")
    HALF_PRICE_SECOND_PIECE,

    /**
     * 组合活动
     */
    @ApiEnumProperty("8：组合商品")
    SUITS_GOODS,

    /**
     * 加价购
     */
    @ApiEnumProperty("9：加价购")
    MARKUP,;


    @JsonCreator
    public static MarketingSubType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
