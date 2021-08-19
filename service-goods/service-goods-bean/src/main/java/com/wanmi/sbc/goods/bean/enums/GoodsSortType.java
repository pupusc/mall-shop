package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 排序方式
 * Created by dyt on 2020/5/22.
 */
@ApiEnum
public enum GoodsSortType {

    @ApiEnumProperty("0按市场价排序")
    MARKET_PRICE,

    @ApiEnumProperty("1按库存排序")
    STOCK,

    @ApiEnumProperty("2按销量排序")
    SALES_NUM;

    @JsonCreator
    public GoodsSortType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
