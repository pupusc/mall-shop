package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作端 D/P
 * Created by jinwei on 27/3/2017.
 */
@ApiEnum(dataType = "java.lang.String")
public enum Department {

    @ApiEnumProperty("0: D 分销商(商户)")
    D("D", "分销商(商户)"),

    @ApiEnumProperty("1: P 零售商")
    P("P", "零售商");

    private String type;

    private String description;

    Department(String type, String description){
        this.type = type;
        this.description = description;
    }

    private static Map<String, Department> departmentMap = new HashMap<>();

    static {
        departmentMap.put("D", D);
        departmentMap.put("P", P);
    }

    @JsonCreator
    public static Department forValue(String type){
        return departmentMap.get(type);
    }

    @JsonValue
    public String toValue(){
        return getType();
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
