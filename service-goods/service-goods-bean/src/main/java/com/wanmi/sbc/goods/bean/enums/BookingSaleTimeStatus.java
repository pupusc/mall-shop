package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 主要围绕预售各种时间的判断
 */
@ApiEnum
public enum BookingSaleTimeStatus {

    @ApiEnumProperty("0：预售进行中")
    BOOKING_RUNNING(0, "预售进行中"),

    @ApiEnumProperty("1：定金支付进行中")
    HAND_SEL_RUNNING(1, "定金支付进行中");

    BookingSaleTimeStatus(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private int status;

    private String msg;

    @JsonCreator
    public static BookingSaleTimeStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
