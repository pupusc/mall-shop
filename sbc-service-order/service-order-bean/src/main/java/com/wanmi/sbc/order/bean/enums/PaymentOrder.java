package com.wanmi.sbc.order.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 订单支付顺序
 */
@ApiEnum(dataType = "java.lang.String")
public enum PaymentOrder {

    @ApiEnumProperty("0: NO_LIMIT 不限")
    NO_LIMIT("NO_LIMIT", "不限"),

    @ApiEnumProperty("1: PAY_FIRST 先款后货")
    PAY_FIRST("PAY_FIRST", "先款后货");

    private String stateId;

    private String description;

    PaymentOrder(String stateId, String description) {
        this.stateId = stateId;
        this.description = description;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}
