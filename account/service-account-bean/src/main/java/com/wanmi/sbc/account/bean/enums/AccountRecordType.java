package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>对账类型枚举</p>
 * Created by of628-wenzhi on 2018-10-12-下午5:34.
 */
@ApiEnum
public enum AccountRecordType {

    /**
     * 收入
     */
    @ApiEnumProperty("收入")
    INCOME,

    /**
     * 退款
     */
    @ApiEnumProperty("退款")
    REFUND;

    @JsonCreator
    public static AccountRecordType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
