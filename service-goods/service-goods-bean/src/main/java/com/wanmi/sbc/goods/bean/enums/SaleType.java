package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 数据类型
 * 0：批发 1零售
 * Created by xiemengnan on 2018/10/22.
 */
@ApiEnum
public enum SaleType {

    @ApiEnumProperty("0: 批发")
    WHOLESALE,

    @ApiEnumProperty("1: 零售")
    RETAIL;
    @JsonCreator
    public  static SaleType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
