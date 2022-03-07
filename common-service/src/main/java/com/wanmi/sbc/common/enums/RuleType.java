package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 0预约规则 1预售规则
 * Created by zhangxiaodong on 2020/05/25.
 */
@ApiEnum
public enum RuleType {
    @ApiEnumProperty("预约规则")
    APPOINTMENT,
    @ApiEnumProperty("预售规则")
    SALE;

    @JsonCreator
    public static RuleType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
