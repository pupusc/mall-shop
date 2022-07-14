package com.soybean.marketing.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ActivityCategoryEnum {

    ACTIVITY_POINT(1, "返积分活动");


    private final Integer code;
    private final String message;

    public static ActivityCategoryEnum getByCode(int code) {
        for (ActivityCategoryEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
