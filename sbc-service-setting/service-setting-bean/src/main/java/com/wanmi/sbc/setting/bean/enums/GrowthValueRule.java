package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 成长值规则（0：自定义获取规则 1：同步积分获取规则）
 */
@ApiEnum
public enum GrowthValueRule {
    @ApiEnumProperty("0：自定义获取规则")
    USER_DEFINED,
    @ApiEnumProperty("1：同步积分获取规则")
    SYNCHRONIZE_POINTS;

    @JsonCreator
    public GrowthValueRule fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
