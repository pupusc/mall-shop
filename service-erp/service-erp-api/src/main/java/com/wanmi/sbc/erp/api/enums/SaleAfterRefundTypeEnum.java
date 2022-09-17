package com.wanmi.sbc.erp.api.enums;

import lombok.Getter;

@Getter
public enum SaleAfterRefundTypeEnum {
    RETURNS_AND_REFUNDS(1, "退货退款"),
    ONLY_REFUNDS(2, "仅退款"),
    EXCHANGE_GOODS(3, "换货"),
    COMPENSATION(4, "补偿-商品"),
    MASTER_ONLY_REFUND(5, "主订单仅退款"),
    GIFT(6, "赠品");
    private Integer code;
    private String desc;

    SaleAfterRefundTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
