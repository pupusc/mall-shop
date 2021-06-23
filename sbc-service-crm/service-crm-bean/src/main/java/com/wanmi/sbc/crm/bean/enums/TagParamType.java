package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public enum TagParamType {
    @ApiEnumProperty("0: top类型")
    TOP,

    @ApiEnumProperty("1：聚合结果类型")
    AGG,

    @ApiEnumProperty("2：查询条件类型")
    WHERE;


    @JsonCreator
    public TagParamType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
