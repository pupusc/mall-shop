package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by yang on 2019/11/10.
 */
public enum ResourceType {
    IMAGE, VIDEO, EXCEL;
    @JsonCreator
    public static ResourceType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}