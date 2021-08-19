package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;
/**
 *  标签参数字段列类型
 *  0：top类型，1：count计数类型，2：sum求和，3：avg平均值，4：max最大值，5：min最小值，6：in包含类型，7：等于，8、区间类，9：多重期间or关系
 */
@ApiModel
public enum TagParamColumnType {
    @ApiEnumProperty("0: top类型")
    TOP,

    @ApiEnumProperty("1：count计数类型")
    COUNT,

    @ApiEnumProperty("2：sum求和")
    SUM,

    @ApiEnumProperty("3：avg平均值")
    AVG,

    @ApiEnumProperty("4：max最大值")
    MAX,

    @ApiEnumProperty("5：min最小值")
    MIN,

    @ApiEnumProperty("6：in包含类型")
    IN,

    @ApiEnumProperty("7：等于")
    EQUALS,

    @ApiEnumProperty("8、区间类")
    BETWEEN,

    @ApiEnumProperty("9：多重期间or关系")
    MULTIPLE_OR;


    @JsonCreator
    public TagParamColumnType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
