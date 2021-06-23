package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 *  订单外部来源
 * @author He
 */
@ApiEnum
public enum OrderExternalSource {
    @ApiEnumProperty("平台订单")
    PLATFORM,
    @ApiEnumProperty("供应商订单")
    SUPPLIER,
    @ApiEnumProperty("ERP")
    ERP,
    ;

    @JsonCreator
    public static OrderExternalSource fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
