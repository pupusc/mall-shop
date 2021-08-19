package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public enum TriggerCondition {
    @ApiEnumProperty("0:无")
    NONE,
    @ApiEnumProperty("1：有访问")
    VISIT,
    @ApiEnumProperty("2：有收藏")
    FOLLOW,
    @ApiEnumProperty("3：有加购")
    ADD,
    @ApiEnumProperty("4：有下单")
    BUY,
    @ApiEnumProperty("5：有付款")
    PAY;


    @JsonCreator
    public TriggerCondition fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    public static TriggerCondition chgValue(int value) {
        return values()[value];
    }
}
