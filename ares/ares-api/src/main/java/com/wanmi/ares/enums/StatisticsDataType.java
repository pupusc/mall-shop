package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author lvzhenwei
 * @Description 流量数据汇总统计类型：0：今天，1：昨天，2：最近七天；3：最近30天；4：按月统计，5：最近30天按周统计，6：最近6个月按周统计
 * @Date 13:54 2019/8/22
 * @Param
 * @return
 **/
public enum StatisticsDataType {
    TODAY, YESTERDAY, SEVEN, THIRTY, MONTH, THIRTY_WEEk, MONTH_WEEk;

    @JsonCreator
    public StatisticsDataType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
