package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 企业购审核状态  0：无状态 1：待审核 2：已审核 3：审核未通过
 */
@ApiEnum
public enum EnterpriseCheckState {
    @ApiEnumProperty("0：无状态")
    INIT,
    @ApiEnumProperty("1：待审核")
    WAIT_CHECK,
    @ApiEnumProperty("2：已审核")
    CHECKED,
    @ApiEnumProperty("3：审核未通过")
    NOT_PASS;

    @JsonCreator
    public EnterpriseCheckState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
