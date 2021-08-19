package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @author huqingjie
 * @date 2021年04月28日
 * @description 同步发货状态时使用的订单类型
 */
@ApiEnum
public enum OrderTypeEnums {

    @ApiEnumProperty("0:普通订单")
    REGULAR_ORDER_ZERO,

    @ApiEnumProperty("1:历史订单")
    HISTORY_ORDER_ONE,

    @ApiEnumProperty("2:周期购订单")
    CYCLE_ORDER_TWO,

    @ApiEnumProperty("3:重置扫描次数")
    RESET_ORDER_THREE;

    @JsonCreator
    public static OrderTypeEnums fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
