package com.wanmi.sbc.goods.mini.enums.goods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WxGoodsStatus {

    WAIT_UPLOAD,
    UPLOAD,
    ON_SHELF,
    OFF_SHELF;

    @JsonCreator
    public static WxGoodsStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
