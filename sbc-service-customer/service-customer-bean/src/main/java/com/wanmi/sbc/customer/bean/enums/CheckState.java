package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 审核状态 0：待审核 1：已审核 2：审核未通过
 * Created by CHENLI on 2017/4/13.
 */
@ApiEnum
public enum CheckState {
    @ApiEnumProperty("0：待审核")
    WAIT_CHECK,
    @ApiEnumProperty("1：已审核")
    CHECKED,
    @ApiEnumProperty("2：审核未通过")
    NOT_PASS;

    @JsonCreator
    public CheckState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
