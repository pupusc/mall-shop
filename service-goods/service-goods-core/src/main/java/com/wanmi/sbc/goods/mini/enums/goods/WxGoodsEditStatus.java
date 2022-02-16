package com.wanmi.sbc.goods.mini.enums.goods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WxGoodsEditStatus {

    WAIT_CHECK,
    ON_CHECK,
    CHECK_FAILED,
    CHECK_SUCCESS;

    @JsonCreator
    public static WxGoodsEditStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
