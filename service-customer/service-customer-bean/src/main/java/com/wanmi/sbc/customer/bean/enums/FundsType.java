package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 会员资金明细-账务类型
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:31
 * @version: 1.0
 */
@ApiEnum
public enum FundsType {

    /**
     * 全部
     */
    @ApiEnumProperty("全部")
    ALL(),

    /**
     * 分销佣金
     */
    @ApiEnumProperty("分销佣金")
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
     * 佣金提成
     */
    @ApiEnumProperty("佣金提成")
    COMMISSION_COMMISSION();

    @JsonCreator
    public static FundsType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
