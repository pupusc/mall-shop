package com.wanmi.sbc.goods.mini.enums.goods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WxGoodsEditStatus {

    //未审核
    WAIT_CHECK,
    //审核中
    ON_CHECK,
    //审核失败
    CHECK_FAILED,
    //审核成功
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
