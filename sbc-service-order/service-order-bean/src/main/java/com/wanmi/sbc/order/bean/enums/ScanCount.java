package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @author huqingjie
 * @date 2021年04月28日
 * @description 同步订单使用的订单扫描次数
 */
@ApiEnum
public enum ScanCount {

    @ApiEnumProperty("0:未扫描")
    COUNT_ZERO,

    @ApiEnumProperty("1:扫描一次")
    COUNT_ONE,

    @ApiEnumProperty("2:扫描二次")
    COUNT_TWO,

    @ApiEnumProperty("3:扫描三次")
    COUNT_THREE;

    @JsonCreator
    public static ScanCount fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
