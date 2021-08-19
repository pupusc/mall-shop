package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * Created by hht on 2017/12/6.
 */
@ApiEnum
public enum SettleStatus {

    @ApiEnumProperty("未结算")
    NOT_SETTLED,

    @ApiEnumProperty("已结算")
    SETTLED,

    @ApiEnumProperty("暂不处理")
    SETTLE_LATER;

    @JsonCreator
    public static SettleStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
