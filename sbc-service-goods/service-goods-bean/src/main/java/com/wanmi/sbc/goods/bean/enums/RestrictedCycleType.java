package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 限售周期的类型 : 0-天  1-周  2-月  3-年  4-终生  5-订单
 * Created by baijz on 2017/4/13.
 * todo 校验时使用的判断
 */
@ApiEnum
public enum RestrictedCycleType {
    @ApiEnumProperty("0：天")
    RESTRICTED_BY_DAY,

    @ApiEnumProperty("1：周")
    RESTRICTED_BY_WEEK,

    @ApiEnumProperty("2：月")
    RESTRICTED_BY_MONTH,

    @ApiEnumProperty("3：年")
    RESTRICTED_BY_YEAR,

    @ApiEnumProperty("4：终生")
    RESTRICTED_BY_ALL_LIFE,

    @ApiEnumProperty("5：订单")
    RESTRICTED_BY_ORDER;


    @JsonCreator
    public RestrictedCycleType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
