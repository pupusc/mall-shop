package com.wanmi.sbc.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 购物车来源
 */
@Getter
@AllArgsConstructor
public enum ShopCartSourceEnum {
    MALL_H5(1, "商城H5"),
    WX_MINI(2, "微信小程序");

    private final Integer code;
    private final String desc;

    public static ShopCartSourceEnum getByCode(int code) {
        for (ShopCartSourceEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
