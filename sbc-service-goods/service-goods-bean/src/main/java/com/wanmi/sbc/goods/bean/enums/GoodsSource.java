package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 商品来源，0供应商，1商家 2 linkedMall
 * Created by Daiyitian on 2020/8/13.
 */
@ApiEnum
public enum GoodsSource {

    @ApiEnumProperty(" 0：供应商")
    PROVIDER,

    @ApiEnumProperty("1：商家")
    SELLER,

    @ApiEnumProperty("2：linkedMall")
    LINKED_MALL,

    @ApiEnumProperty("3：platform")
    PLATFORM;

    @JsonCreator
    public GoodsSource fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
