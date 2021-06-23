package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:53 2019/2/27
 * @Description: 佣金返利优先级
 */
@ApiEnum
public enum CommissionPriorityType {

    @ApiEnumProperty("0：邀请人优先")
    INVITOR,

    @ApiEnumProperty("1：店铺优先")
    STORE;

    @JsonCreator
    public static CommissionPriorityType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


}
