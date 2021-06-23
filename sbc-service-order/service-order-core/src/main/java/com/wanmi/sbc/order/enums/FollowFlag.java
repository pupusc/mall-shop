package com.wanmi.sbc.order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 收藏类型
 * 0：所有 1:采购 2:收藏
 * Created by daiyitian on 2017/3/22.
 */
public enum FollowFlag {
    ALL, PURCHASE, FOLLOW;
    @JsonCreator
    public FollowFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
