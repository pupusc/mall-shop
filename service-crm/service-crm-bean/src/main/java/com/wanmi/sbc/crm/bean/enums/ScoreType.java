package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public enum ScoreType {
    @ApiEnumProperty(" 0: 参数得分")
    PARAM_SCORE,

    @ApiEnumProperty("1：平均分")
    AVG_SCORE;


    @JsonCreator
    public ScoreType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
