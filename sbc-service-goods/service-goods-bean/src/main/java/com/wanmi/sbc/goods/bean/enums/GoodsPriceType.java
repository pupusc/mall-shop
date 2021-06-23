package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 设价类型
 * 0按客户 1按订货量 2按市场价
 * Created by zhangjin on 2017/3/22.
 */
@ApiEnum
public enum GoodsPriceType {

    @ApiEnumProperty("0按客户")
    CUSTOMER,

    @ApiEnumProperty("1按订货量")
    STOCK,

    @ApiEnumProperty("2按市场价")
    MARKET;
    @JsonCreator
    public static GoodsPriceType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
