package com.wanmi.sbc.bookmeta.enums;

import lombok.Getter;

/**
 * @author Liang Jun
 * @date 2022-05-24 15:20:00
 */
@Getter
public enum BookRcmmdTypeEnum {
    AWARD(1, "获奖推荐"),
    EDITOR(2, "编辑推荐"),
    MEDIA(3, "媒体推荐"),
    ORGAN(4, "专业机构推荐"),
    EXPERT(5, "名家推荐"),
    QUOTE(6, "书中引用推荐"),
    DRAFT(7, "讲稿引用推荐"),
    MENTION(8, "书中提到的人物"),

    WENMIAO(9, "文喵君推荐"),
    XUANSHUREN(10, "选书人推荐");


    private Integer code;
    private String desc;

    BookRcmmdTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BookRcmmdTypeEnum getEnumByCode(Integer code) {
        for (BookRcmmdTypeEnum item : BookRcmmdTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
