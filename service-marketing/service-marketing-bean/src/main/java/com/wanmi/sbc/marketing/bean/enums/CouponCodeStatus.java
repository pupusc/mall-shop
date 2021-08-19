package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 优惠券码状态
 * 0可用 1未达到使用门槛 2本单商品不可用 3未到可用时间
 * Created by gaomuwei on 2018/9/26.
 */
@ApiEnum
public enum CouponCodeStatus {

    /**
     * 可用
     */
    @ApiEnumProperty("0：可用")
    AVAILABLE(0),

    /**
     * 未达到使用门槛
     */
    @ApiEnumProperty("1：未达到使用门槛")
    UN_REACH_PRICE(1),

    /**
     * 本单商品不可用
     */
    @ApiEnumProperty("2：本单商品不可用")
    NO_AVAILABLE_SKU(2),

    /**
     * 未到可用时间
     */
    @ApiEnumProperty("3：未到可用时间")
    UN_REACH_TIME(3);

    private int type;

    CouponCodeStatus(int type) {
        this.type = type;
    }

    @JsonCreator
    public static CouponCodeStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.type;
    }

}
