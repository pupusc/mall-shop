package com.wanmi.sbc.goods.info.model.entity;

import lombok.Data;

@Data
public class GoodsStockInfo {
    public GoodsStockInfo(String goodsInfoId, String goodsId, String goodsNo) {
        this.goodsInfoId = goodsInfoId;
        this.goodsId = goodsId;
        this.goodsNo = goodsNo;
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
}
