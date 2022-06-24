package com.soybean.elastic.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SearchSpuNewSortTypeEnum {

    DEFAULT(0, "默认"),
    SCORE(1, "书评人评分"),
    NEW_ADDED(2, "新上"),
    FAVOR_COMMENT(3, "好评"),
    HIGH_PRICE(4, "高价"),
    LOWER_PRICE(5, "低价"),
    ;

    private Integer code;

    private String message;


    public static SearchSpuNewSortTypeEnum get(Integer code) {
        for (SearchSpuNewSortTypeEnum spuSortType : values()) {
            if (Objects.equals(spuSortType.getCode(), code)) {
                return spuSortType;
            }
        }
        return null;
    }
}
