package com.soybean.mall.cart.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * ES商品实体类
 * 以SKU维度
 * Created by dyt on 2017/4/21.
 */
@Data
@ApiModel
public class PromoteFitGoodsResultVO implements Serializable {
    /**
     * spuId
     */
    private String goodsId;
    /**
     * spu名称
     */
    private String goodsName;
    /**
     * skuId
     */
    private String goodsInfoId;
    /**
     * sku名称
     */
    private String goodsInfoName;
    /**
     * 规格名称
     */
    private String specText;
    /**
     * 选购数量
     */
    private Integer buyCount;
    /**
     * 最大数量
     */
    private Integer maxCount;
}
