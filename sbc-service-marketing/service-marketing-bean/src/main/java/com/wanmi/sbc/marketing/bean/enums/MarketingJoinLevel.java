package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum MarketingJoinLevel {

    /**
     * 付费会员
     */
    @ApiEnumProperty("-5：付费会员")
    PAID_CARD_CUSTOMER(-5),

    /**
     * 企业会员
     */
    @ApiEnumProperty("-4：企业会员")
    ENTERPRISE_CUSTOMER(-4),

    /**
     * 指定人群
     */
    @ApiEnumProperty("-3：指定人群")
    SPECIFY_GROUP(-3),
    /**
     * 指定客户
     */
    @ApiEnumProperty("-2：指定客户")
    SPECIFY_CUSTOMER(-2),

    /**
     * 所有客户
     */
    @ApiEnumProperty("-1：所有客户")
    ALL_CUSTOMER(-1),

    /**
     * 所有等级
     */
    @ApiEnumProperty("0：所有等级")
    ALL_LEVEL(0),

    /**
     * 其他等级
     */
    @ApiEnumProperty("1：其他等级")
    LEVEL_LIST(1);

    private int type;

    MarketingJoinLevel(int type){
        this.type = type;
    }

    @JsonCreator
    public static MarketingJoinLevel fromValue(int value) {
        for (MarketingJoinLevel level : values()) {
            if (level.type == value) {
                return level;
            }
        }

        return null;
    }

    @JsonValue
    public int toValue() {
        return this.type;
    }
}
