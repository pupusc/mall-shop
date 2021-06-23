package com.wanmi.sbc.pay.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 0处理中(退款状态)/未支付(支付状态) 1成功 2失败
 * Created by sunkun on 2017/8/2.
 */
@ApiEnum
public enum TradeStatus {

    @ApiEnumProperty("处理中(退款状态)/未支付(支付状态)")
    PROCESSING,

    @ApiEnumProperty("成功")
    SUCCEED,

    @ApiEnumProperty("失败")
    FAILURE;

    @JsonCreator
    public TradeStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
