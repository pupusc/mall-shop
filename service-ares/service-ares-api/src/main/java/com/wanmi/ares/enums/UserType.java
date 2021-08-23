package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 流量统计报表用户类型：0：网站访问用户数，1：sku访问用户数
 */
public enum UserType {
    ALL, SKU;

    @JsonCreator
    public UserType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
