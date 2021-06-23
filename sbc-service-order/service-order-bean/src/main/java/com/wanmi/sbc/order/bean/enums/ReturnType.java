package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>退单类型 0：退货 1：退款</p>
 * Created by of628-wenzhi on 2017-05-19-下午3:37.
 */
@ApiEnum
public enum ReturnType {

    /**
     * 退货
     */
    @ApiEnumProperty("0: 退货")
    RETURN,

    /**
     * 退款
     */
    @ApiEnumProperty("1: 退款")
    REFUND;

    @JsonCreator
    public static ReturnType forValue(String name) {
        return ReturnType.valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
