package com.wanmi.sbc.crm.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public enum DateType {

    @ApiEnumProperty("微信小程序")
    MONDAY("周一"),

    @ApiEnumProperty("周二")
    TUESDAY("周二"),

    @ApiEnumProperty("周三")
    WEDNESDAY("周三"),

    @ApiEnumProperty("周四")
    THURSDAY("周四"),

    @ApiEnumProperty("周五")
    FRIDAY("周五"),

    @ApiEnumProperty("周六")
    SATURDAY("周六"),

    @ApiEnumProperty("周日")
    SUNDAY("周日");


    private final String value;

    DateType(String value){
        this.value = value;
    }

    public String toValue() {
        return this.value;
    }
}
