package com.wanmi.sbc.common.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author lvzhenwei
 * @Description 订单类型枚举，由于现在订单类型比较多，所以用orderType来区分订单的类型
 * @Date 15:16 2019/5/14
 * @Param
 * @return
 **/
@ApiEnum(dataType = "java.lang.String")
public enum OrderType {

    @ApiEnumProperty("0: 普通订单")
    NORMAL_ORDER("NORMAL_ORDER", "普通订单"),

    @ApiEnumProperty("1: 积分订单")
    POINTS_ORDER("POINTS_ORDER", "积分订单"),

    @ApiEnumProperty("2: 所有订单")
    ALL_ORDER("All_ORDER", "所有订单"),

    @ApiEnumProperty("3: 周期购订单")
    CYCLE_BUY_ORDER("CYCLE_BUY_ORDER", "周期购订单");

    private String orderTypeId;

    private String description;

    OrderType(String orderTypeId, String description) {
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
