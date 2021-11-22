package com.wanmi.sbc.marketing.bean.dto;

import lombok.Data;

@Data
public class MarketingPointBuyLevelDto {

    private Long id;

    /**
     * 金额
     */
    private Double money;

    /**
     * 定价
     */
    private Double price;

    /**
     * 积分
     */
    private Integer pointNeed;

    /**
     * 商品sku
     */
    private String skuId;
}
