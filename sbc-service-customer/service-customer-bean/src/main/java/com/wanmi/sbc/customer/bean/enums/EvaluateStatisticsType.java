package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @author liutao
 * 统计类型 0：商品评分，1：服务评分，2：物流评分
 * @date 2019/2/27 5:05 PM
 */
@ApiEnum
public enum EvaluateStatisticsType {
    /**
     * 商品评分
     */
    @ApiEnumProperty("商品评分")
    GOODS,

    /**
     * 服务评分
     */
    @ApiEnumProperty("服务评分")
    SERVER,

    /**
     * 物流评分
     */
    @ApiEnumProperty("物流评分")
    LOGISTICS;

    @JsonCreator
    public static EvaluateStatisticsType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
