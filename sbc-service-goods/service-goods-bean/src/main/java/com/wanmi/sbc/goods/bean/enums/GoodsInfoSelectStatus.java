package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 审核状态 0：上架中 1：下架中 2：待审核及其他
 * Created by Daiyitian on 2017/4/13.
 */
@ApiEnum
public enum GoodsInfoSelectStatus {

    @ApiEnumProperty("0：上架中")
    ADDED,

    @ApiEnumProperty("1：下架中")
    NOT_ADDED,

    @ApiEnumProperty("2：待审核及其他")
    OTHER;
    @JsonCreator
    public GoodsInfoSelectStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}