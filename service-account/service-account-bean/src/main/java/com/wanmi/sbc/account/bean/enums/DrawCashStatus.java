package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 提现状态(0:未提现,1:提现失败,2:提现成功)
 * @author chenyufei
 */
@ApiEnum
public enum DrawCashStatus {
    /**
     * 未提现
     */
    @ApiEnumProperty("未提现")
    WAIT,

    /**
     * 提现失败
     */
    @ApiEnumProperty("提现失败")
    FAIL,

    /**
     * 提现成功
     */
    @ApiEnumProperty("提现成功")
    SUCCESS;

    @JsonCreator
    public static DrawCashStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
