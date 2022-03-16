package com.wanmi.sbc.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 赠品标记
 */
@Getter
@AllArgsConstructor
public enum OrderTagEnum {
    GIFT("GIFT", "赠品");

    private final String code;
    private final String desc;

    public static OrderTagEnum getByCode(int code) {
        for (OrderTagEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
