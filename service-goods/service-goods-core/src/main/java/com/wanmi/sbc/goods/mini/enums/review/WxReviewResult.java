package com.wanmi.sbc.goods.mini.enums.review;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WxReviewResult {

    SUCCESS,
    FAILED;

    @JsonCreator
    public static WxReviewResult fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
