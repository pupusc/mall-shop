package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束，5：进行中&未开始
 */
@ApiEnum
public enum PresellSaleStatus {

    @ApiEnumProperty("0：全部")
    ALL,

    @ApiEnumProperty("1：进行中")
    STARTED,

    @ApiEnumProperty("2：暂停中")
    PAUSED,

    @ApiEnumProperty("3：未开始")
    NOT_START,

    @ApiEnumProperty("4：已结束")
    ENDED,

    @ApiEnumProperty("5：进行中&未开始")
    S_NS;

    @JsonCreator
    public PresellSaleStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
