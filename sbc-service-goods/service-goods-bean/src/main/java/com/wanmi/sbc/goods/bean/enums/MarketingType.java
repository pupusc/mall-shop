package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 10:46 2019-02-11
 * @Description: 营销类型
 */
@ApiEnum(dataType = "java.lang.String")
public enum MarketingType {
    /**
     * 满减
     */
    @ApiEnumProperty("满减")
    REDUCTION("满减优惠"),

    /**
     * 满折
     */
    @ApiEnumProperty("满折")
    DISCOUNT("满折优惠"),

    /**
     * 满赠
     */
    @ApiEnumProperty("满赠")
    GIFT("满赠优惠");

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
