package com.wanmi.sbc.common.enums.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 账户资产通知枚举类
 */
@ApiEnum
public enum AccoutAssetsType {

    @ApiEnumProperty(" 0：优惠券到账提醒")
    COUPON_RECEIPT("COUPON_RECEIPT","优惠券到账提醒"),

    @ApiEnumProperty("1：优惠券过期提醒")
    COUPON_EXPIRED("COUPON_EXPIRED","优惠券过期提醒"),

    @ApiEnumProperty("2：积分到账提醒")
    INTEGRAL_RECEIPT("INTEGRAL_RECEIPT","积分到账提醒"),

    @ApiEnumProperty("3：积分过期提醒")
    INTEGRAL_EXPIRED("INTEGRAL_EXPIRED","积分过期提醒"),

    @ApiEnumProperty("4：积分过期再次提醒")
    INTEGRAL_EXPIRED_AGAIN("INTEGRAL_EXPIRED_AGAIN","积分过期再次提醒"),

    @ApiEnumProperty("5：成长值到账提醒")
    GROWTH_VALUE_RECEIPT("GROWTH_VALUE_RECEIPT","成长值到账提醒"),

    @ApiEnumProperty("6：余额账户变更提醒")
    BALANCE_CHANGE("BALANCE_CHANGE","余额账户变更提醒"),

    @ApiEnumProperty("7：提现申请提交成功通知")
    BALANCE_WITHDRAW_APPLY_FOR("BALANCE_WITHDRAW_APPLY_FOR","提现申请提交成功通知"),

    @ApiEnumProperty("8：提现成功通知")
    BALANCE_WITHDRAW_SUCCESS("BALANCE_WITHDRAW_SUCCESS","提现成功通知"),

    @ApiEnumProperty("9：提现申请驳回通知")
    BALANCE_WITHDRAW_REJECT("BALANCE_WITHDRAW_REJECT","提现申请驳回通知");

    private String type;

    private String description;

    AccoutAssetsType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    @JsonCreator
    public AccoutAssetsType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
