package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 审核状态 0：新增 1：修改 2：删除
 * Created by sunkun on 2017/9/20.
 */
public enum OperationType {
    INSERT, UPDATE, DELETE;

    @JsonCreator
    public OperationType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
