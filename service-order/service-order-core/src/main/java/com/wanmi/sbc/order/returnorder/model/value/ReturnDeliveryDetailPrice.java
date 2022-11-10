package com.wanmi.sbc.order.returnorder.model.value;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReturnDeliveryDetailPrice {

    /**
     * 实际运费积分
     */
    private BigDecimal deliveryPointPrice;

    /**
     * 实际运费积分
     */
    private Long deliveryPoint;

    /**
     * 实际运费现金金额
     */
    private BigDecimal deliveryPayPrice;
}