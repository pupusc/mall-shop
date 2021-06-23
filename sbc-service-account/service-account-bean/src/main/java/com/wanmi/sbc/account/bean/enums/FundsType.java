package com.wanmi.sbc.account.bean.enums;

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
@ApiEnum(dataType = "java.lang.String")
public enum FundsType {

    /**
     * 全部
     */
    @ApiEnumProperty("全部")
    ALL("全部"),

    /**
     * 分销佣金
     */
    @ApiEnumProperty("分销佣金")
    DISTRIBUTION_COMMISSION("分销佣金"),

    /**
     * 佣金提现
     */
    @ApiEnumProperty("佣金提现")
    COMMISSION_WITHDRAWAL("佣金提现"),

    /**
     * 邀新奖励
     */
    @ApiEnumProperty("邀新奖励")
    INVITE_NEW_AWARDS("邀新奖励"),

    /**
     * 佣金提成
     */
    @ApiEnumProperty("佣金提成")
    COMMISSION_COMMISSION("佣金提成"),

    /**
     * 余额支付
     */
    @ApiEnumProperty("余额支付")
    BALANCE_PAY("余额支付"),

    /**
     * 余额支付退款
     */
    @ApiEnumProperty("余额支付退款")
    BALANCE_PAY_REFUND("余额支付退款");

    private String desc;

    FundsType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static FundsType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
