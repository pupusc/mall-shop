package com.wanmi.sbc.bookmeta.enums;

import lombok.Getter;

/**
 * @author Liang Jun
 * @date 2022-05-24 15:20:00
 */
@Getter
public enum LabelSceneEnum {
    FIT_TARGET(1, "适读对象");

    private Integer code;
    private String desc;

    LabelSceneEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LabelSceneEnum getEnumByCode(Integer code) {
        for (LabelSceneEnum item : LabelSceneEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
