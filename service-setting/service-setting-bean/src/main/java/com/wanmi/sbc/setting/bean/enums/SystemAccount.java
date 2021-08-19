package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * Created by zhangjin on 2017/6/23.
 */
@ApiEnum(dataType = "java.lang.String")
public enum SystemAccount {

    /**
     * BOSS
     */
    @ApiEnumProperty("BOSS")
    SYSTEM("system");

    private String desc;

    SystemAccount(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static SystemAccount forValue(String name) {
        return SystemAccount.valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
