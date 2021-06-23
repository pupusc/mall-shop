package com.wanmi.sbc.customer.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by zhangjin on 2017/6/23.
 */
public enum SystemAccount {

    /**
     * BOSS
     */
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
