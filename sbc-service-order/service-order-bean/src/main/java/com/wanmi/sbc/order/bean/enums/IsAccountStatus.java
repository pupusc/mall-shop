package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @author liutao
 * @date 2019/4/13 11:07 AM
 */
@ApiEnum
public enum  IsAccountStatus {

    @ApiEnumProperty("否")
    NO,
    @ApiEnumProperty("是")
    YES,
    @ApiEnumProperty("失败")
    FAIL;

    @JsonCreator
    public static IsAccountStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
