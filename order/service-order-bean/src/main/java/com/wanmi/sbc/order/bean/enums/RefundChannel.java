package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>退款渠道</p>
 */
@ApiEnum
public enum RefundChannel {

    /**
     * 定金
     */
    @ApiEnumProperty("定金")
    EARNEST,

    /**
     * 尾款
     */
    @ApiEnumProperty("尾款")
    TAIL;

    @JsonCreator
    public RefundChannel fromValue(String name) {
        return valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
