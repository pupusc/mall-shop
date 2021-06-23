package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 订单来源--区分h5,pc,app,小程序,代客下单
 */
@ApiEnum(dataType = "java.lang.String")
public enum OrderSource {

    /**
     * 代客下单
     */
    @ApiEnumProperty("0: 代客下单")
    SUPPLIER("代客下单"),

    /**
     * 会员h5端下单
     */
    @ApiEnumProperty("1: 会员h5端下单")
    WECHAT("会员h5端下单"),

    /**
     * 会员pc端下单
     */
    @ApiEnumProperty("2: 会员pc端下单")
    PC("会员pc端下单"),

    /**
     * 会员APP端下单
     */
    @ApiEnumProperty("3: 会员APP端下单")
    APP("会员APP端下单"),

    /**
     * 会员小程序端下单
     */
    @ApiEnumProperty("4: 会员小程序端下单")
    LITTLEPROGRAM("会员小程序端下单");

    private String desc;

    OrderSource(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static OrderSource forValue(String name) {
        return OrderSource.valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
