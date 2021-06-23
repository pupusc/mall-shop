package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 审核状态
 * @author chenyufei
 */
@ApiEnum
public enum AuditStatus {

    /**
     * 待审核
     */
    @ApiEnumProperty("待审核")
    WAIT,

    /**
     * 拒绝
     */
    @ApiEnumProperty("拒绝")
    REJECT,

    /**
     * 支付宝
     */
    @ApiEnumProperty("通过")
    PASS;

    @JsonCreator
    public static AuditStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
