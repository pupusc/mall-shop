package com.sbc.wanmi.erp.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @program: sbc-background
 * @description: ERP退款状态
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-08 13:49
 **/
public enum  RefundStatus {
    @ApiEnumProperty("0：未退款")
    UN_REFUND,

    @ApiEnumProperty("1：退款成功")
    REFUND_SUCCESS,

    @ApiEnumProperty("2：退款中")
    REFUND_PROCESSING;

    @JsonCreator
    public RefundStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
