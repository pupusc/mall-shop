package com.wanmi.sbc.quartz.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum TaskStatus {

    /**
     * 未开始
     */
    NOT_STARTED,

    /**
     * 进行中
     */
    STARTING,

    /**
     * 已结束
     */
    END;

    @JsonCreator
    public static TaskStatus fromValue(int ordinal) {
        return TaskStatus.values()[ordinal];
    }

    @JsonValue
    public Integer toValue() {
        return this.ordinal();
    }

}
