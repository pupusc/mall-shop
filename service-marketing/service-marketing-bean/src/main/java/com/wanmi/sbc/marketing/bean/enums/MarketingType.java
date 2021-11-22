package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 营销活动类型
 */
@ApiEnum(dataType = "java.lang.String")
public enum MarketingType {
    /**
     * 满减
     */
    @ApiEnumProperty("0：满减优惠")
    REDUCTION("满减优惠"),

    /**
     * 满折
     */
    @ApiEnumProperty("1：满折优惠")
    DISCOUNT("满折优惠"),

    /**
     * 满赠
     */
    @ApiEnumProperty("2：满赠优惠")
    GIFT("满赠优惠"),

    /**
     * 一口价优惠
     */
    @ApiEnumProperty("3：一口价优惠活动")
    BUYOUT_PRICE("一口价优惠"),

    /**
     * 第二件半价优惠活动
     */
    @ApiEnumProperty("4：第二件半价优惠活动")
    HALF_PRICE_SECOND_PIECE("第二件半价优惠活动"),

    @ApiEnumProperty("5：秒杀")
    FLASH_SALE("秒杀"),

    @ApiEnumProperty("6：组合套餐")
    SUITS("组合套餐"),
    @ApiEnumProperty("7：加价购")
    MARKUP("加价购"),

    @ApiEnumProperty("8：积分换购")
    POINT_BUY("积分换购");

    MarketingType(String desc) {
        this.desc = desc;
    }

    /**
     * 描述信息
     */
    private String desc;

    @JsonCreator
    public static MarketingType fromValue(int value) {
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
