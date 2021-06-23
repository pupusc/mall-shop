package com.wanmi.sbc.common.enums.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 分销业务通知枚举类
 */
@ApiEnum
public enum DistributionType {

    @ApiEnumProperty(" 0：推广订单支付成功通知")
    PROMOTE_ORDER_PAY_SUCCESS("PROMOTE_ORDER_PAY_SUCCESS","推广订单支付成功通知"),

    @ApiEnumProperty("1：好友注册成功通知【无邀新奖励限制时】")
    FRIEND_REGISTER_SUCCESS_NO_REWARD("FRIEND_REGISTER_SUCCESS_NO_REWARD","好友注册成功通知【无邀新奖励限制时】"),

    @ApiEnumProperty(" 2：好友注册成功通知【有邀新奖励限制时】")
    FRIEND_REGISTER_SUCCESS_HAS_REWARD("FRIEND_REGISTER_SUCCESS_HAS_REWARD","好友注册成功通知【有邀新奖励限制时】"),

    @ApiEnumProperty("3：邀新奖励到账通知【有邀新奖励限制时】")
    INVITE_CUSTOMER_REWARD_RECEIPT("INVITE_CUSTOMER_REWARD_RECEIPT","邀新奖励到账通知【有邀新奖励限制时】");


    private String type;

    private String description;

    DistributionType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    @JsonCreator
    public DistributionType fromValue(int value) {
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
