package com.soybean.mall.order.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 订单来源--区分h5,pc,app,小程序,代客下单
 */
@AllArgsConstructor
@Getter
public enum OrderSourceEnum {

    H5_MALL("H5_MALL", "H5下单"),
    VIDEO_MALL("VIDEO_MALL", "视频号下单"),
    MINIAPP_MALL("MINIAPP_MALL", "小程序下单"),
    PLATFORM_MALL("PLATFORM_MALL", "平台下单");

    private String code;
    private String message;


    public static OrderSourceEnum get(String code) {
        for (OrderSourceEnum value : OrderSourceEnum.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        return H5_MALL;
    }

//    /**
//     * 代客下单
//     */
//    @ApiEnumProperty("0: 代客下单")
//    SUPPLIER("代客下单"),
//
//    /**
//     * 会员h5端下单
//     */
//    @ApiEnumProperty("1: 会员h5端下单")
//    WECHAT("会员h5端下单"),

//    /**
//     * 会员pc端下单
//     */
//    @ApiEnumProperty("2: 会员pc端下单")
//    PC("会员pc端下单"),

//    /**
//     * 会员APP端下单
//     */
//    @ApiEnumProperty("3: 会员APP端下单")
//    APP("会员APP端下单"),

//    /**
//     * 会员小程序端下单
//     */
//    @ApiEnumProperty("4: 会员小程序端下单")
//    LITTLEPROGRAM("会员小程序端下单");
//
//    private String desc;
//
//    OrderSource(String desc) {
//        this.desc = desc;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    @JsonCreator
//    public static OrderSource forValue(String name) {
//        return OrderSource.valueOf(name);
//    }
//
//    @JsonValue
//    public String toValue() {
//        return this.name();
//    }
}
