package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 银行状态 0: 启用 1:禁用
 * Created by zhangjin on 2017/3/19.
 */
@ApiEnum(dataType = "java.lang.String")
public enum AccountStatus {

    /**
     * 启用 0
     */
    @ApiEnumProperty("启用")
    ENABLE("启用"),

    /**
     * 禁用 1
     */
    @ApiEnumProperty("禁用")
    DISABLE("禁用");

    private String desc;

    AccountStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public AccountStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
