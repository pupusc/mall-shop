package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 营销优先级类型，按优先级由高到低排列
 */
@ApiEnum
public enum  MarketingLevelType {

    /**
     * 积分价
     */
    @ApiEnumProperty("0：积分价")
    POINTS_PRICE("积分价"),

    /**
     * 预约预售
     */
    @ApiEnumProperty("1：预约预售")
    APPOINTMENT_OR_BOOKING("预约预售"),

    /**
     * 秒杀
     */
    @ApiEnumProperty("2：秒杀")
    FLASH_SALE("秒杀"),

    /**
     * 分销
     */
    @ApiEnumProperty("3：分销")
    DISTRIBUTION("分销"),

    /**
     * 企业价
     */
    @ApiEnumProperty("4：企业价")
    ENTER_PRISE_PRICE("企业价"),

    /**
     * 拼团
     */
    @ApiEnumProperty("5：拼团")
    GROUPON("拼团");

    MarketingLevelType(String desc) {
        this.desc = desc;
    }

    /**
     * 描述信息
     */
    private String desc;

    @JsonCreator
    public static MarketingLevelType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    public String getDesc() {
        return desc;
    }
}
