package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 邮箱配置启用状态（0：停用，1：启用）
 */
@ApiEnum
public enum EmailStatus {
    @ApiEnumProperty("0：停用")
    DISABLE,
    @ApiEnumProperty("1：启用")
    ENABLE;

    @JsonCreator
    public EmailStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
