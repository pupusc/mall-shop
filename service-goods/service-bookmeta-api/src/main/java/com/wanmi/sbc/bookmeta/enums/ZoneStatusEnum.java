package com.wanmi.sbc.bookmeta.enums;

import lombok.Getter;

/**
 * @author Liang Jun
 * @date 2022-05-24 15:20:00
 */
@Getter
public enum ZoneStatusEnum {
    ENABLE(1, "启用"),
    DISABLE(2, "停用");

    private Integer code;
    private String desc;

    ZoneStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ZoneStatusEnum getEnumByCode(Integer code) {
        for (ZoneStatusEnum item : ZoneStatusEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
