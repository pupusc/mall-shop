package com.wanmi.sbc.goods.info.model.entity;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;


/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:53 2019/5/20
 * @Description: 单品参数对象
 */
@Data
public class GoodsInfoLiveGoods {

    public GoodsInfoLiveGoods(String goodsInfoId, String goodsId, Long stock) {
        this.goodsInfoId = goodsInfoId;
        this.goodsId = goodsId;
        this.stock = stock;
    }

    /**
     * 单品id
     */
    private String goodsInfoId;

    /**
     * SPU编号
     */
    private String goodsId;

    /**
     * 商品库存
     */
    private Long stock;

    /**
     * 规格名称
     */
    private String specText;

}
