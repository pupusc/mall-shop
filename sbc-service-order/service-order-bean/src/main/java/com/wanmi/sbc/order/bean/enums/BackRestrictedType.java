package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum(dataType = "java.lang.String")
public enum BackRestrictedType {

    @ApiEnumProperty("0: 订单取消")
    ORDER_CANCEL,

    @ApiEnumProperty("1: 超时未支付 ")
    ORDER_SETTING_TIMEOUT_CANCEL,

    @ApiEnumProperty("2: 退货 ")
    RETURN_ORDER,

    @ApiEnumProperty("3: 退款 ")
    REFUND_ORDER,

    @ApiEnumProperty("4: 审批未通过 ")
    APPROVAL_REJECT;



    @JsonCreator
    public static BackRestrictedType forValue(String name) {
        return BackRestrictedType.valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }



}
