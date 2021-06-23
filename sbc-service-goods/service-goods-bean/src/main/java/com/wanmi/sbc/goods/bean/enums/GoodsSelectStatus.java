package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 审核状态 0：上架中 1：下架中 2：部分上架中 3：待审核及其他
 *
 * @author lipeng
 * @dateTime 2018/11/5 上午9:43
 */
@ApiEnum
public enum  GoodsSelectStatus {

    @ApiEnumProperty("0：上架中")
    ADDED,

    @ApiEnumProperty("1：下架中")
    NOT_ADDED,

    @ApiEnumProperty("2：部分上架中")
    PART_ADDED,

    @ApiEnumProperty("3：待审核及其他")
    OTHER;
    @JsonCreator
    public GoodsSelectStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
