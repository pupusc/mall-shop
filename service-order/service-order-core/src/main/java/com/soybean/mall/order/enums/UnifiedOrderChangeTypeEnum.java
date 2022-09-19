package com.soybean.mall.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnifiedOrderChangeTypeEnum {
    DISCOUNT("DISCOUNT", "优惠"),
    CHANGE_PRICE("CHANGE_PRICE", "改价"),
    MARKETING("MARKETING", "营销活动"),
    VIP_PRICE_DIFF("VIP_PRICE_DIFF", "会员价");

    private String type;
    private String msg;
}