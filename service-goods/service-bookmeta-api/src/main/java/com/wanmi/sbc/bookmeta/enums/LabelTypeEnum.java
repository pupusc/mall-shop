package com.wanmi.sbc.bookmeta.enums;

import lombok.Getter;

/**
 * @author Liang Jun
 * @date 2022-05-24 15:20:00
 */
@Getter
public enum LabelTypeEnum {
    CATEGORY(1, "目录"),
    LABEL(2, "标签");

    private Integer code;
    private String desc;

    LabelTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LabelTypeEnum getEnumByCode(Integer code) {
        for (LabelTypeEnum item : LabelTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
