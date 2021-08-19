package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;


@ApiEnum
public enum AppointmentStatus {

    @ApiEnumProperty("0：全部")
    ALL(0, "全部"),
    @ApiEnumProperty("1：进行中")
    RUNNING(1, "进行中"),
    @ApiEnumProperty("2：已暂停")
    SUSPENDED(2, "已暂停"),
    @ApiEnumProperty("3：未开始")
    NO_START(3, "未开始"),
    @ApiEnumProperty("4：已结束")
    END(4, "已结束"),
    @ApiEnumProperty("5：未开始和进行中")
    NO_START_AND_RUNNING(5, "未开始和进行中");

    AppointmentStatus(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private int status;

    private String msg;

    @JsonCreator
    public static AppointmentStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
