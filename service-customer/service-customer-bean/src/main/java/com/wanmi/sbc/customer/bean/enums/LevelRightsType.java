package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 会员等级权益类型 0等级徽章 1专属客服 2会员折扣 3券礼包 4返积分 5自定义
 */
@ApiEnum
public enum LevelRightsType {
    @ApiEnumProperty("0等级徽章")
    LEVEL_BADGE,
    @ApiEnumProperty("1专属客服")
    EXCLUSIVE_SERVICE,
    @ApiEnumProperty("2会员折扣")
    CUSTOMER_DISCOUNT,
    @ApiEnumProperty("3券礼包")
    COUPON_GIFT,
    @ApiEnumProperty("4返积分")
    RETURN_POINTS,
    @ApiEnumProperty("5自定义")
    CUSTOMIZE;

    @JsonCreator
    public LevelRightsType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
