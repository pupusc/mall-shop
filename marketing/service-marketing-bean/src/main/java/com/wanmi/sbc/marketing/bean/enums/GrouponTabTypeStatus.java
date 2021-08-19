package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 页面tab类型，0: 即将开始, 1: 进行中, 2: 已结束，3：待审核，4：审核失败
 */
public enum GrouponTabTypeStatus {
    @ApiEnumProperty("0：即将开始")
    NOT_START,

    @ApiEnumProperty("1：进行中")
    STARTED,

    @ApiEnumProperty("2：已结束")
    ENDED,

    @ApiEnumProperty("3：待审核")
    WAIT_CHECK,

    @ApiEnumProperty("4：审核失败")
    NOT_PASS,

    @ApiEnumProperty("5：即将开始和进行中")
    WILL_AND_ALREADY_START;


    @JsonCreator
    public GrouponTabTypeStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public String toValue() {
        return String.valueOf(this.ordinal());
    }

}
