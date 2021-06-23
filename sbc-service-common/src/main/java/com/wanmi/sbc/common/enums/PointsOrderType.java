package com.wanmi.sbc.common.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 积分订单类型枚举
 *
 * @Author minchen
 **/
@ApiEnum(dataType = "java.lang.String")
public enum PointsOrderType {

    @ApiEnumProperty("0: 积分兑换商品")
    POINTS_GOODS("POINTS_GOODS", "积分兑换商品"),

    @ApiEnumProperty("1: 积分兑换优惠券")
    POINTS_COUPON("POINTS_COUPON", "积分兑换优惠券");

    private String orderTypeId;

    private String description;

    PointsOrderType(String orderTypeId, String description) {
        this.orderTypeId = orderTypeId;
        this.description = description;
    }

    public String getOrderTypeId() {
        return orderTypeId;
    }

    public String getDescription() {
        return description;
    }
}
