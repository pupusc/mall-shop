package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 拼团订单状态：0：待成团 ，1：已成团，2：拼团失败
 */
public enum GrouponOrderStatus {

    @ApiEnumProperty("0: 待开团")
    UNPAY,

    @ApiEnumProperty("1: 待成团")
    WAIT,

    @ApiEnumProperty("2: 已成团")
    COMPLETE,

    @ApiEnumProperty("3: 拼团失败")
    FAIL;

    @JsonCreator
    public GrouponOrderStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
