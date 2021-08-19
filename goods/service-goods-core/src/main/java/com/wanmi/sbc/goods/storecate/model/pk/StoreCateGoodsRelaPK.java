package com.wanmi.sbc.goods.storecate.model.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品-店铺分类联合主键类
 * Created by bail on 2017/11/13.
 */
@Data
public class StoreCateGoodsRelaPK implements Serializable {

    /**
     * 商品标识
     */
    private String goodsId;

    /**
     * 店铺分类标识
     */
    private Long storeCateId;

}

