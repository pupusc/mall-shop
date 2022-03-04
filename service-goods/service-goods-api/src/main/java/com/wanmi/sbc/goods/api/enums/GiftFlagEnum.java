package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 赠品标记
 */
@Getter
@AllArgsConstructor
public enum GiftFlagEnum {
    TRUE(1, "是"),
    FALSE(0, "否");

    private final Integer code;
    private final String message;

    public static GiftFlagEnum getByCode(int code) {
        for (GiftFlagEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
