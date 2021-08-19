package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 开票状态 0待开票 1 已开票
 * Created by CHENLI on 2017/5/5.
 */
@ApiEnum
public enum InvoiceState {

    @ApiEnumProperty("待开票")
    WAIT,

    @ApiEnumProperty("已开票")
    ALREADY;

    @JsonCreator
    public InvoiceState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
