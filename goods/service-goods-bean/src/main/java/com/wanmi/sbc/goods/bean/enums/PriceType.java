package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 数据类型
 * 0：spu数据 1sku数据 2:企业购sku
 * Created by daiyitian on 2017/3/22.
 */
@ApiEnum
public enum PriceType {

    @ApiEnumProperty("0：spu数据")
    SPU,

    @ApiEnumProperty("1: sku数据")
    SKU,

    @ApiEnumProperty("2: 企业购SKU")
    ENTERPRISE_SKU;


    @JsonCreator
    public static PriceType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
