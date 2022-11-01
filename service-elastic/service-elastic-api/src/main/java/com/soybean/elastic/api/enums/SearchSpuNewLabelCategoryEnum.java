package com.soybean.elastic.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SearchSpuNewLabelCategoryEnum {

    FREE_DELIVERY_49(1, "满49元包邮", 5, 49),
    FREE_DELIVERY(2, "包邮", 0, 0)
    ;

    private Integer code;

    private String message;

    private Object threshold;

    private Object realValue;


    public static SearchSpuNewLabelCategoryEnum get(Integer code) {
        for (SearchSpuNewLabelCategoryEnum spuSortType : values()) {
            if (Objects.equals(spuSortType.getCode(), code)) {
                return spuSortType;
            }
        }
        return null;
    }
}
