package com.wanmi.sbc.pay.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>交易类型</p>
 * Created by of628-wenzhi on 2017-08-07-下午2:57.
 */
@ApiEnum
public enum TradeType {

    /**
     * 支付
     */
    @ApiEnumProperty("支付")
     PAY,

    /**
     * 退款
     */
    @ApiEnumProperty("退款")
     REFUND;

    @JsonCreator
    public TradeType fromValue(String name) {
        return valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
