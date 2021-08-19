package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author lvzhenwei
 * @Description 按周统计：0:最近30天按周统计，1：最近6个月按周统计
 * @Date 17:07 2019/8/26
 * @Param
 * @return
 **/
public enum StatisticsWeekType {
    THIRTY_WEEk, MONTH_WEEk;

    @JsonCreator
    public StatisticsWeekType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
