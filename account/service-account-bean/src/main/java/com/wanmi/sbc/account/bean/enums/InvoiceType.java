package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 发票类型 0普通发票 1增值税专用发票
 * Created by CHENLI on 2017/5/5.
 */
@ApiEnum
public enum InvoiceType {

    @ApiEnumProperty("普通发票")
    NORMAL,

    @ApiEnumProperty("增值税专用发票")
    SPECIAL;

    @JsonCreator
    public InvoiceType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
