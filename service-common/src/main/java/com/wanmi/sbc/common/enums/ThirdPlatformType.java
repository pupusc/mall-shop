package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 *
 * @Author: daiyitian
 * @Date: Created In 上午9:49 2019/3/1
 * @Description: 第三方平台类型
 */
@ApiEnum
public enum ThirdPlatformType {

    @ApiEnumProperty("LINKED_MALL")
    LINKED_MALL;

    @JsonCreator
    public static ThirdPlatformType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
