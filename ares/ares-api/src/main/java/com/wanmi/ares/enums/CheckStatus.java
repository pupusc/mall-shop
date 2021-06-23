package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 审核状态 0：待审核 1：已审核 2：审核失败 3：禁售中
 * Created by bail on 2017/01/23.
 */
public enum CheckStatus {
    WAIT_CHECK,CHECKED,NOT_PASS,FORBADE;
    @JsonCreator
    public CheckStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
