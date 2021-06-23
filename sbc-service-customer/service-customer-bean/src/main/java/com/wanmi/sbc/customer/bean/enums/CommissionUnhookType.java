package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:53 2019/2/27
 * @Description: 佣金提成脱钩
 */
@ApiEnum
public enum CommissionUnhookType {

    @ApiEnumProperty("0：不限")
    UNLIMITED,

    @ApiEnumProperty("1：分销员和邀请人平级时脱钩")
    EQUAL,

    @ApiEnumProperty("2：分销员高于邀请人等级时脱钩")
    HIGHER;

    @JsonCreator
    public static CommissionUnhookType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


}
