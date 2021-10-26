package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;

public enum PublishState {

    NOT_ENABLE, ENABLE;

    public static PublishState fromValue(int value) {
        return values()[value];
    }

    public int toValue() {
        return this.ordinal();
    }
}
