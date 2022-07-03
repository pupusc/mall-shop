package com.wanmi.sbc.bookmeta.enums;

import lombok.Getter;

/**
 * @author Liang Jun
 * @date 2022-05-24 15:20:00
 */
@Getter
public enum FigureTypeEnum {
    //1作者/译者/绘画人/作序人；2编辑；3名家；4专业机构；5媒体；
    AUTHOR(1, "作者/译者/绘画人/作序人"),
    EDITOR(2, "编辑"),
    FAMOUS(3, "名人"),
    ORGAN(4, "机构"),
    MEDIA(5, "媒体");

    private Integer code;
    private String desc;

    FigureTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FigureTypeEnum getEnumByCode(Integer code) {
        for (FigureTypeEnum item : FigureTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
