package com.soybean.elastic.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SearchSpuNewCategoryEnum {

    BOOK(1, "图书"),
    SPU(2, "商品"),
    ;

    private Integer code;

    private String message;


    public static SearchSpuNewCategoryEnum get(Integer code) {
        for (SearchSpuNewCategoryEnum spuSortType : values()) {
            if (Objects.equals(spuSortType.getCode(), code)) {
                return spuSortType;
            }
        }
        return null;
    }
}
