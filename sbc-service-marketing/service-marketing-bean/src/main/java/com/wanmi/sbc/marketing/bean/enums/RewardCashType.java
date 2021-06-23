package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:56 2019/2/27
 * @Description: 奖励上限类型
 */
@ApiEnum
public enum RewardCashType {

    @ApiEnumProperty("0：不限")
    UNLIMITED,

    @ApiEnumProperty("1：限人数")
    LIMITED;

    @JsonCreator
    public RewardCashType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
