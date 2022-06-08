package com.wanmi.sbc.bookmeta.enums;

import lombok.Getter;

/**
 * @author Liang Jun
 * @date 2022-05-24 15:20:00
 */
@Getter
public enum DataDictCateEnum {
    BOOK_LANGUAGE("book_language", "书籍语言"),
    FIGURE_CATE("figure_cate", "人物分类"),
    BOOK_GROUP_CATE("book_group_cate", "书组分类"),
    FIGURE_COUNTRY("figure_country", "人物国籍"),
    BOOK_PAPER("book_paper", "籍纸张"),
    BOOK_BIND("book_bind", "书籍装帧"),
    LABEL_SCENE("label_scene", "标签场景");

    private String code;
    private String desc;

    DataDictCateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DataDictCateEnum getEnumByCode(String code) {
        for (DataDictCateEnum item : DataDictCateEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
