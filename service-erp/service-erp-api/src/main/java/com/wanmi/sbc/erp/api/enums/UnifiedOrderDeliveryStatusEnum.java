package com.wanmi.sbc.erp.api.enums;

import lombok.Getter;

@Getter
public enum UnifiedOrderDeliveryStatusEnum {
    SIGNED(1, "已签收"),
    UNSIGN(2, "未签收");
    private Integer status;
    private String desc;

    UnifiedOrderDeliveryStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
