package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author xuyunpeng
 * @Description 消息类型枚举类
 * @Date 14:28 2019/1/6
 * @Param
 * @return
 **/
@ApiEnum
public enum NoticeType {

    @ApiEnumProperty(" 0：账号安全")
    ACCOUNT_SECURITY,

    @ApiEnumProperty("1：账户资产")
    ACCOUNT_BALANCE,

    @ApiEnumProperty("2：订单进度")
    ORDER_PROGRESS,

    @ApiEnumProperty("3：退单进度")
    RETURN_ORDER_PROGRESS,

    @ApiEnumProperty("4：分销业务")
    DISTRIBUTION_BUSINESS;
    @JsonCreator
    public NoticeType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
