package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum PriceAdjustmentType {

    @ApiEnumProperty("0 市场价")
    MARKET,

    @ApiEnumProperty("1 等级价")
    LEVEL,

    @ApiEnumProperty("2 阶梯价")
    STOCK,

    @ApiEnumProperty("3 供货价")
    SUPPLY;

    @JsonCreator
    public static PriceAdjustmentType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
