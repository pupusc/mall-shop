package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: songhanlin
 * @Date: Created In 14:13 2020/3/2
 * @Description: 增值服务枚举
 */
@ApiEnum(dataType = "java.lang.String")
public enum VASConstants {

    /**
     * 暂时无用, 有计划合并进来
     */
    @ApiEnumProperty("增值服务-CRM-设置")
    VAS_CRM_SETTING("vas_crm_setting"),


    /**
     * 增值服务-企业购-设置
     */
    @ApiEnumProperty("增值服务-企业购-设置")
    VAS_IEP_SETTING("vas_iep_setting");

    private final String value;

    public static Map<String, VASConstants> map = new HashMap<>();

    static {
        Arrays.stream(VASConstants.values()).forEach(
                t -> map.put(t.value, t)
        );
    }

    VASConstants(String value) {
        this.value = value;
    }

    @JsonValue
    public String toValue() {
        return value;
    }

    @JsonCreator
    public static VASConstants fromValue(String value) {
        return map.get(value);
    }

}
