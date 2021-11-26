package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public enum TagType {
    @ApiEnumProperty("0: 偏好类")
    PREFERENCE,

    @ApiEnumProperty("1：指标值")
    QUOTA,

    @ApiEnumProperty("2：指标值范围")
    RANGE,

    @ApiEnumProperty("3：综合")
    MULTIPLE;


    @JsonCreator
    public TagType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
