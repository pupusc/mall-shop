package com.soybean.mall.order.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum PaymentPayTypeEnum {
    XIAN_JIN(1, "现金"),
    ZHI_DOU(2, "知豆"),
    JI_FEN(3, "积分"),
    ZHI_HUI_BI(4, "智慧币");
    private Integer payTypeCode;
    private String payTypeName;

    PaymentPayTypeEnum(Integer payTypeCode, String payTypeName) {
        this.payTypeCode = payTypeCode;
        this.payTypeName = payTypeName;
    }

    public static PaymentPayTypeEnum byCode(Integer payTypeCode) {
        return Arrays.stream(PaymentPayTypeEnum.values())
                .filter(item -> Objects.equals(item.getPayTypeCode(), payTypeCode))
                .findFirst().orElse(null);
    }
}
