package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 系统设置的redis缓存key
 * Created by daiyitian on 2017/3/22.
 */
@ApiEnum(dataType = "java.lang.String")
public enum SettingRedisKey {
    /**
     * SYSTEM_POINTS_CONFIG:积分设置缓存
     */
    SYSTEM_POINTS_CONFIG("SYSTEM_POINTS_CONFIG");


    private final String value;

    SettingRedisKey(String value) {
        this.value = value;
    }

    @JsonValue
    public String toValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}