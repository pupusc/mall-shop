package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * Created by hht on 2017/12/6.
 */
@ApiEnum
public enum TradeType {

    @ApiEnumProperty("普通")
    NORMAL_TRADE,

    @ApiEnumProperty("促销")
    MARKET_TRADE;

    @JsonCreator
    public static TradeType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
