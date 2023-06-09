package com.wanmi.sbc.goods.api.response.goods;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class NewBookPointRedisResponse implements Serializable {

    private Integer id;

    /**
     * SkuId
     */
    private String skuId;

    /**
     * SkuNo
     */
    private String skuNo;

    /**
     * SpuId
     */
    private String SpuId;

    /**
     * spuNo
     */
    private String spuNo;

    /**
     * 排序数
     */
    private Integer sortNum;

    /**
     * 标签值
     */
    private String skuTag;


    /**
     *  数量
     */
    private Integer num;


    /**
     *  市场价
     */
    private BigDecimal marketPrice;


    /**
     * 最新计算的会员价
     * 为空，以市场价为准
     */
    private BigDecimal salePrice;


    /**
     * 图片
     */
    private String goodsInfoImg;

    /**
     * 商品名
     */
    private String goodsInfoName;
}