package com.wanmi.sbc.erp.api.enums;

import lombok.Getter;

@Getter
public enum SaleAfterCompensationTypeEnum {
    PRESENT(1, "赠品"),
    COMPENSATION_GOODS(2, "补偿-商品");

    private Integer code;
    private String desc;

    SaleAfterCompensationTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
