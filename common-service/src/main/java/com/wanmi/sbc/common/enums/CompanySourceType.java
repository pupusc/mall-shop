package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 商家来源类型
 * Created by dyt on 2020/08/11.
 */
@ApiEnum
public enum CompanySourceType {

    @ApiEnumProperty("0:商城入驻")
    SBC_MALL,

    @ApiEnumProperty("1:LinkedMall")
    LINKED_MALL;

    @JsonCreator
    public static CompanySourceType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
