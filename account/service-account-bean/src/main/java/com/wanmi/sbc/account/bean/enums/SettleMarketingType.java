package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum(dataType = "java.lang.String")
public enum SettleMarketingType {
    /**
     * 满减
     */
    @ApiEnumProperty("满减优惠")
    REDUCTION("满减优惠"),

    /**
     * 满折
     */
    @ApiEnumProperty("满折优惠")
    DISCOUNT("满折优惠"),

    /**
     * 满赠
     */
    @ApiEnumProperty("满赠优惠")
    GIFT("满赠优惠"),

    /**
     * 一口价优惠
     */
    @ApiEnumProperty("一口价优惠活动")
    BUYOUT_PRICE("一口价优惠"),

    /**
     * 第二件半价优惠活动
     */
    @ApiEnumProperty("第二件半价优惠活动")
    HALF_PRICE_SECOND_PIECE("第二件半价优惠活动"),


    @ApiEnumProperty("秒杀")
    FLASH_SALE("秒杀"),


    @ApiEnumProperty("组合套餐")
    SUITS("组合套餐");


    SettleMarketingType(String desc) {
        this.desc = desc;
    }

    /**
     * 描述信息
     */
    private String desc;

    @JsonCreator
    public static SettleMarketingType fromValue(int value) {
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
