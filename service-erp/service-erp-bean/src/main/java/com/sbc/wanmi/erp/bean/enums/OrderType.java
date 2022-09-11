package com.sbc.wanmi.erp.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @program: sbc-background
 * @description: 订单类型
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-08 13:49
 **/
public enum OrderType {
    @ApiEnumProperty("Sales：销售订单")
    SALES("Sales","销售订单"),

    @ApiEnumProperty("Return：换货订单")
    RETURN("Return","换货订单"),

    @ApiEnumProperty("Charge：费用订单")
    CHARGE("Charge","费用订单"),

    @ApiEnumProperty("Delivery：补发货订单")
    DELIVERY("Delivery","补发货订单"),

    @ApiEnumProperty("Invoice：补发票订单")
    INVOICE("Invoice","补发票订单");


    private String key;

    private String value;

    OrderType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
