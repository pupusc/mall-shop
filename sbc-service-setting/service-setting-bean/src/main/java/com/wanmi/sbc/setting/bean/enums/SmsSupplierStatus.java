package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 是否开启(0未开启 1已开启)
 * Created by aqlu on 15/12/4.
 */
@ApiEnum
public enum SmsSupplierStatus {
    @ApiEnumProperty("0未开启")
    DISABLE,
    @ApiEnumProperty("1已开启")
    ENABLE;

    @JsonCreator
    public static SmsSupplierStatus forValue(String ordinal) {
        return SmsSupplierStatus.values()[Integer.parseInt(ordinal)];
    }

    @JsonValue
    public String toValue() {
        return String.valueOf(this.ordinal());
    }
}
