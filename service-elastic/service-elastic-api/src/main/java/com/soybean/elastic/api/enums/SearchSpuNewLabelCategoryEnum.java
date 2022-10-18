package com.soybean.elastic.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SearchSpuNewLabelCategoryEnum {

    FREE_DELIVERY(1, "49元包邮", 5);
    ;

    private Integer code;

    private String message;

    private Object threshold;


    public static SearchSpuNewLabelCategoryEnum get(Integer code) {
        for (SearchSpuNewLabelCategoryEnum spuSortType : values()) {
            if (Objects.equals(spuSortType.getCode(), code)) {
                return spuSortType;
            }
        }
        return null;
    }
}
