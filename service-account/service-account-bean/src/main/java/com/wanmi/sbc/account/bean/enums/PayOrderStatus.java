package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 收款状态0:已收款 1.未收款 2.待确认
 * Created by zhangjin on 2017/3/20.
 */
@ApiEnum
public enum PayOrderStatus {

    @ApiEnumProperty("已收款")
    PAYED,

    @ApiEnumProperty("未收款")
    NOTPAY,

    @ApiEnumProperty("待确认")
    TOCONFIRM;

    @JsonCreator
    public PayOrderStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
