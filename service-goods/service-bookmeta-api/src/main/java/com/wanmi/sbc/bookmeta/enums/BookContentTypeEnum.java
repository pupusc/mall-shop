package com.wanmi.sbc.bookmeta.enums;

import lombok.Getter;

/**
 * @author Liang Jun
 * @date 2022-05-24 15:20:00
 */
@Getter
public enum BookContentTypeEnum {
    INTRODUCE(1, "简介"),
    CATALOGUE(2, "目录"),
    EXTRACT(3, "摘录"),
    PRELUDE(4, "序言"),
    PREFACE(5, "前言");

    private Integer code;
    private String desc;

    BookContentTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BookContentTypeEnum getEnumByCode(Integer code) {
        for (BookContentTypeEnum item : BookContentTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
