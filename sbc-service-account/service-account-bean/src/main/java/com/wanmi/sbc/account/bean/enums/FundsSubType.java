package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 会员资金明细-账务子类型
 */
@ApiEnum
public enum FundsSubType {

    /**
     * 全部
     */
    @ApiEnumProperty("全部")
    ALL(),

    /**
     * 推广返利
     */
    @ApiEnumProperty("推广返利")
    DISTRIBUTION_COMMISSION(),

    /**
     * 佣金提现
     */
    @ApiEnumProperty("佣金提现")
    COMMISSION_WITHDRAWAL(),

    /**
     * 邀新奖励
     */
    @ApiEnumProperty("邀新奖励")
    INVITE_NEW_AWARDS(),

    /**
     * 自购返利
     */
    @ApiEnumProperty("自购返利")
    SELFBUY_COMMISSION(),

    /**
     * 推广提成
     */
    @ApiEnumProperty("推广提成")
    PROMOTION_COMMISSION(),

    /**
     * 余额支付
     */
    @ApiEnumProperty("余额支付")
    BALANCE_PAY(),
    /**
     * 余额支付退款
     */
    @ApiEnumProperty("余额支付退款")
    BALANCE_PAY_REFUND();


    @JsonCreator
    public static FundsSubType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
