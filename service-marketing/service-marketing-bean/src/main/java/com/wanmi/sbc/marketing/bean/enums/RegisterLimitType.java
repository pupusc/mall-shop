package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:53 2019/2/27
 * @Description: 注册限制
 */
@ApiEnum
public enum RegisterLimitType {

    @ApiEnumProperty("0：不限")
    UNLIMITED,

    @ApiEnumProperty("1：仅限邀请注册")
    INVITE;

    @JsonCreator
    public RegisterLimitType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


}
