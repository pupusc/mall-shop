package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

public enum LiveRoomStatus {
    @ApiEnumProperty("0:直播中")
    ZERO,
    @ApiEnumProperty("1:暂停")
    ONE,
    @ApiEnumProperty("2:异常")
    TOW,
    @ApiEnumProperty("3:未开始")
    THREE,
    @ApiEnumProperty("4:已结束")
    FOUR,
    @ApiEnumProperty("5:禁播")
    FIVE,
    @ApiEnumProperty("6:已过期")
    SIX;

    @JsonCreator
    public LiveRoomStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
