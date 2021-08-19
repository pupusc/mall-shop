package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 0:按市场价 1:按会员等级设价 2:按购买数量设价
 */
@ApiEnum
public enum EnterprisePriceType {

    @ApiEnumProperty("0按市场价")
    MARKET,

    @ApiEnumProperty("1按会员等级设价")
    CUSTOMER,

    @ApiEnumProperty("2按购买数量设价")
    STOCK;
    @JsonCreator
    public static EnterprisePriceType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
