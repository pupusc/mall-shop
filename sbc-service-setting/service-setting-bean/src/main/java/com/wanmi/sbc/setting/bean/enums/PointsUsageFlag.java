package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 积分使用方式 0:订单抵扣,1:商品抵扣
 * Created by daiyitian on 2020/4/8
 */
@ApiEnum
public enum PointsUsageFlag {
    @ApiEnumProperty("订单抵扣")
    ORDER,
    @ApiEnumProperty("商品抵扣")
    GOODS;

    @JsonCreator
    public static PointsUsageFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
