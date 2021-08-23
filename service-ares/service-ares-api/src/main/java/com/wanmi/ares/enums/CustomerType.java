package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *  客户类型,0:平台客户,1:商家客户
 */
public enum CustomerType {
    PLATFORM, SUPPLIER;
    @JsonCreator
    public CustomerType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
