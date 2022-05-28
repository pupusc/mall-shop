package com.wanmi.sbc.goods.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 同步状态
 */
@AllArgsConstructor
@Getter
public enum HasAssistantGoodsValidEnum {


    NO_SYNC(0, "不同步"),
    SYNC(1, "同步");

    private final int code;
    private final String message;

    public static HasAssistantGoodsValidEnum getByCode(int code) {
        for (HasAssistantGoodsValidEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
