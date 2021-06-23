package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 店铺状态 0、开启 1、关店
 * Created by CHENLI on 2017/11/2.
 */
public enum StoreState {
    OPENING,CLOSED;
    @JsonCreator
    public StoreState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
