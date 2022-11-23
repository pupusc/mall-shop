package com.wanmi.sbc.erp.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopCenterEnum {

    SHOPCENTER(21L, "商城");

    private Long code;
    private String message;
}
