package com.soybean.mall.order.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liang Jun
 * @date 2022-06-26 21:47:00
 */
@Data
public class SettlementResultVO$GoodsInfo {
    /**
     * 店铺id
     */
    private String spuId;
    private String spuName;
    private String skuId;
    private String skuName;
    /**
     * 商品图片
     */
    private String pic;
    /**
     * 购买数量
     */
    private Long num;
    /**
     * 单位
     */
    private String unit;
    /**
     * 成交价格
     */
    private BigDecimal payPrice;
    /**
     * 商品属性的定价
     */
    private Double propPrice;
    /**
     * 规格描述信息
     */
    private String specDetails;
    /**
     * 商品体积
     */
    private BigDecimal goodsCubage;
    /**
     * 商品重量
     */
    private BigDecimal goodsWeight;
}
