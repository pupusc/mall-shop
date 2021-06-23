package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:56 2019/2/27
 * @Description: 分销限制条件
 */
@ApiEnum
public enum DistributionLimitType {

    @ApiEnumProperty("0：不限")
    UNLIMITED,

    @ApiEnumProperty("1：仅限有效邀新")
    EFFECTIVE;

    @JsonCreator
    public DistributionLimitType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
