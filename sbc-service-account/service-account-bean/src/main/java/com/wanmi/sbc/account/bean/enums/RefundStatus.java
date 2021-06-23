package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 退款单状态：0.待退款 1.拒绝退款 2.已退款 3 商家申请退款(待平台退款)
 * Created by zhangjin on 2017/3/21.
 */
@ApiEnum
public enum RefundStatus {

    @ApiEnumProperty("待退款")
    TODO,

    @ApiEnumProperty("拒绝退款")
    REFUSE,

    @ApiEnumProperty("已退款")
    FINISH,

    @ApiEnumProperty("商家申请退款(待平台退款)")
    APPLY;

    @JsonCreator
    public RefundStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
