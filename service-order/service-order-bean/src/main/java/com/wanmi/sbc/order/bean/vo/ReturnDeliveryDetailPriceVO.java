package com.wanmi.sbc.order.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReturnDeliveryDetailPriceVO {

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