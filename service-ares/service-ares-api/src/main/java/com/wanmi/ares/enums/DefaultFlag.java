package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 是否默认,0否1是
 * Created by daiyitian on 2017/3/22.
 */
public enum DefaultFlag {
    NO, YES;
    @JsonCreator
    public DefaultFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
