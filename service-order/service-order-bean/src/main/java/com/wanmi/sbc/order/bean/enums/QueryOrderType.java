package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 筛选订单类型
 * Created by dyt on 2020/6/16.
 */
@ApiEnum
public enum QueryOrderType {

    @ApiEnumProperty("拼团订单")
    GROUPON,

    @ApiEnumProperty("秒杀订单")
    FLASH_SALE,

    @ApiEnumProperty("预售订单")
    BOOKING_SALE,

    @ApiEnumProperty("积分价订单")
    BUY_POINTS_ORDER,

    @ApiEnumProperty("虚拟商品订单")
    VIRTUAL_GOODS_ORDER,

    @ApiEnumProperty("电子卡券订单")
    VIRTUAL_COUPON_ORDER,

    @ApiEnumProperty("周期购订单")
    CYCLE_BUY_ORDER,

    ;

    @JsonCreator
    public QueryOrderType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
