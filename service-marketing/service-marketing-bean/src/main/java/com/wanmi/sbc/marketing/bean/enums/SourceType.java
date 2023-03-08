package com.wanmi.sbc.marketing.bean.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SourceType {
    APP_ORDER(1, "App订单"),
    REDEEM_CARD(2, "实体卡"),
    EXCHANGE_CODE(3, "兑换码"),
    GIFT_CARD(4, "礼品卡"),
    GROUP_ORDER(5, "团单"),
    OPEN_PLATFORM(6, "开放平台订单"),
    PRE_PURCHASE(7, "预采购"),
    MIX_ORDER(8, "组合销售"),
    SHOW_CENTER(9, "电商中台订单");


    private Integer code;
    private String name;

    SourceType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SourceType getByCode(Integer code) {
        return Arrays.stream(SourceType.values()).filter(s -> s.getCode().equals(code)).findFirst().orElse(null);
    }

    public static String getNameByCode(Integer code) {
        return Arrays.stream(SourceType.values()).filter(s -> s.getCode().equals(code)).map(SourceType::getName).findFirst().orElse("未知");
    }
}