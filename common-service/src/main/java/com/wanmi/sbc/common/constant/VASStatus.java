package com.wanmi.sbc.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 17:36 2020/1/15
 * @Description: 增值服务状态
 */
@ApiEnum(dataType = "java.lang.String")
public enum VASStatus {

    /**
     * 启用
     */
    @ApiEnumProperty("enable：启用")
    ENABLE("enable"),

    /**
     * 禁用
     */
    @ApiEnumProperty("disable：禁用")
    DISABLE("disable");

    VASStatus(String desc) {
        this.desc = desc;
    }

    /**
     * 描述信息
     */
    private String desc;

    @JsonCreator
    public static VASStatus fromValue(String value) {
        return valueOf(value);
    }

    @JsonValue
    public String toValue() {
        return desc;
    }
}
