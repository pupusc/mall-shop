package com.soybean.mall.order.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-26 21:47:00
 */
@Data
public class SettlementResultVO$GoodsInfo {
    /**
     * 店铺id
     */
    private Long storeId;
    private String spuId;
    private String spuName;
    private String skuId;
    private String skuName;
    private String skuNo;
    /**
     * 运费模板ID
     */
    private Long freightTempId;
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
    private BigDecimal price;
    /**
     * 商品属性的定价
     */
    private Double propPrice;
    /**
     * 商品原价 - 建议零售价
     */
    private BigDecimal originalPrice;
    /**
     * 商品价格 - 会员价 & 阶梯设价
     */
    private BigDecimal levelPrice;
    /**
     * 平摊小计 - 最初由 levelPrice*num（购买数量） 计算
     */
    private BigDecimal splitPrice;
    /**
     * 规格描述信息
     */
    private String specDetails;

    /**
     * 商品参加的营销活动id集合
     */
    private List<Long> marketingIds = new ArrayList<>();

    /**
     * 商品参加的营销活动levelid集合
     */
    private List<Long> marketingLevelIds = new ArrayList<>();
}
