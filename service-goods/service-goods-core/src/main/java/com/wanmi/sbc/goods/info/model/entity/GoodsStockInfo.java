package com.wanmi.sbc.goods.info.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsStockInfo {
    public GoodsStockInfo(String goodsInfoId, String goodsId, String goodsNo,BigDecimal costPrice,Integer costPriceSyncFlag,String goodsInfoName) {
        this.goodsInfoId = goodsInfoId;
        this.goodsId = goodsId;
        this.goodsNo = goodsNo;
        this.costPrice = costPrice;
        this.costPriceSyncFlag =costPriceSyncFlag;
        this.goodsInfoName = goodsInfoName;
    }

    /**
     * 单品id
     */
    private String goodsInfoId;

    /**
     * SPU编号
     */
    private String goodsId;


    private String goodsNo;

    private BigDecimal costPrice;

    private Integer costPriceSyncFlag;

    private String goodsInfoName;
}
