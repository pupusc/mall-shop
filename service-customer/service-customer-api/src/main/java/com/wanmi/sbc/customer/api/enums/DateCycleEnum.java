package com.wanmi.sbc.customer.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>按日期周期查询参数枚举</p>
 * Created by of628-wenzhi on 2019-04-18-11:10.
 */
public enum DateCycleEnum {

    /**
     * 近7天
     */
    LATEST7DAYS,

    /**
     * 最近30天
     */
    LATEST30DAYS,

    /**
     * 自然月
     */
    MONTH;

    @JsonCreator
    public static DateCycleEnum fromValue(int ordinal) {
        return DateCycleEnum.values()[ordinal];
    }

    @JsonValue
    public Integer toValue() {
        return this.ordinal();
    }

}
