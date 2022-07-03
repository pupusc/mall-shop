package com.soybean.elastic.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SearchBookListCategoryEnum {

    RANKING_LIST(1, "榜单"),
    BOOK_LIST(2, "书单"),
    ;

    private Integer code;

    private String message;


    public static SearchBookListCategoryEnum get(Integer code) {
        for (SearchBookListCategoryEnum spuSortType : values()) {
            if (Objects.equals(spuSortType.getCode(), code)) {
                return spuSortType;
            }
        }
        return null;
    }
}
