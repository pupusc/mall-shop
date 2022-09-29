package com.wanmi.sbc.erp.api.enums;

import lombok.Getter;

@Getter
public enum UnifiedOrderObjectTypeEnum {
    ORDER("ORD_ORDER", "订单"),
    ORDER_ITEM("ORD_ITEM", "订单"),
    SALE_AFTER_ORDER("SALE_AFTER_ORDER", "售后单"),
    SALE_AFTER_ITEM("SALE_AFTER_ITEM", "售后子单"),
    SALE_AFTER_DETAIL("SALE_AFTER_DETAIL", "售后子单明细"),
    ORDER_PACK("ORD_PACK", "商品包"),;

    private String objectType;
    private String msg;

    UnifiedOrderObjectTypeEnum(String objectType, String msg) {
        this.objectType = objectType;
        this.msg = msg;
    }
}
