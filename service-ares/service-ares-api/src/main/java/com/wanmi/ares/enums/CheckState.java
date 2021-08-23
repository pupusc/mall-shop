package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 审核状态 0：待审核 1：已审核 2：审核未通过
 * Created by sunkun on 2017/9/19.
 */
public enum CheckState {
    WAIT_CHECK,CHECKED,NOT_PASS;

    @JsonCreator
    public CheckState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
