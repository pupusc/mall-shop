package com.wanmi.sbc.bookmeta.enums;

import lombok.Getter;

/**
 * @author Liang Jun
 * @date 2022-05-24 15:20:00
 */
@Getter
public enum BookFigureTypeEnum {
    AUTHOR(1, "作者"),
    TRANSLATOR(2, "译者"),
    PAINTER(3, "绘者");

    private Integer code;
    private String desc;

    BookFigureTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BookFigureTypeEnum getEnumByCode(Integer code) {
        for (BookFigureTypeEnum item : BookFigureTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
