package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 11:17 AM 2018/9/12
 * @Description: 优惠券活动类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券
 */
@ApiEnum
public enum CouponActivityType {

    /**
     * 全场赠券
     */
    @ApiEnumProperty("0：全场赠券")
    ALL_COUPONS(0),

    /**
     * 指定赠券
     */
    @ApiEnumProperty("1：指定赠券")
    SPECIFY_COUPON(1),

    /**
     * 进店赠券
     */
    @ApiEnumProperty("2：进店赠券")
    STORE_COUPONS(2),

    /**
     * 注册赠券
     */
    @ApiEnumProperty("3：注册赠券")
    REGISTERED_COUPON(3),

    /**
     * 权益赠券
     */
    @ApiEnumProperty("4：权益赠券")
    RIGHTS_COUPON(4),

    /**
     * 分销邀新赠券
     */
    @ApiEnumProperty("5：分销邀新赠券")
    DISTRIBUTE_COUPON(5),

    /**
     * 积分兑换券
     */
    @ApiEnumProperty("6: 积分兑换券")
    POINTS_COUPON(6),

    /**
     * 企业会员注册赠券
     */
    @ApiEnumProperty("7: 企业会员注册赠券")
    ENTERPRISE_REGISTERED_COUPON(7);

    private int type;

    CouponActivityType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static CouponActivityType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.type;
    }


}
