package com.wanmi.sbc.goods.info.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by of628-wenzhi on 2020-12-14-9:06 下午.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMarketingPrice {
    private String goodsInfoId;
    private String goodsInfoNo;
    private String goodsInfoName;
    private String goodsId;
    private String specText;
    private Integer saleType;
    private BigDecimal marketingPrice;
    private BigDecimal supplyPrice;

    public GoodsMarketingPrice(String goodsInfoId, String goodsInfoNo, String goodsInfoName, String goodsId, Integer saleType, BigDecimal marketingPrice, BigDecimal supplyPrice) {
        this.goodsInfoId = goodsInfoId;
        this.goodsInfoNo = goodsInfoNo;
        this.goodsInfoName = goodsInfoName;
        this.goodsId = goodsId;
        this.saleType = saleType;
        this.marketingPrice = marketingPrice;
        this.supplyPrice = supplyPrice;
    }
}
