package com.wanmi.sbc.customer.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 0：顶层类的父节点
 * Created by zhangjin on 2017/3/22.
 */
public enum CateParentTop {
    ZERO;
    @JsonCreator
    public CateParentTop fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
