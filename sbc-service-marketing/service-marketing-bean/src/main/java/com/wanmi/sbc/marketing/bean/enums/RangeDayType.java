package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 10:21 AM 2018/9/12
 * @Description: 起止时间类型
 */
@ApiEnum
public enum RangeDayType {

    /**
     * 按起止时间
     */
    @ApiEnumProperty("0：按起止时间")
    RANGE_DAY(0),

    /**
     * 按N天有效
     */
    @ApiEnumProperty("1：按N天有效")
    DAYS(1);

    private int type;

    RangeDayType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static RangeDayType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.type;
    }
}
