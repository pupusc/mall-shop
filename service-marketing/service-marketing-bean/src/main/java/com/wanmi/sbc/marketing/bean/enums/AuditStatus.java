package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 是否审核通过，0：待审核，1：审核通过，2：审核不通过
 */
public enum AuditStatus {
    @ApiEnumProperty("0：待审核")
    WAIT_CHECK,

    @ApiEnumProperty("1：已审核")
    CHECKED,

    @ApiEnumProperty("2：审核失败")
    NOT_PASS;

    @JsonCreator
    public static AuditStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public String toValue() {
        return String.valueOf(this.ordinal());
    }

}
