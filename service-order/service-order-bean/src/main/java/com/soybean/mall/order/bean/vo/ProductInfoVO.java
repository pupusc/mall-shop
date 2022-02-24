package com.soybean.mall.order.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductInfoVO implements Serializable {
    private static final long serialVersionUID = 3137859029592049343L;
    /**
     * 商品Id
     */
    private String outProductId;
    /**
     * skuId
     */
    private String outSkuId;
    /**
     * 数量
     */
    private Long productNum;
    /**
     * 生成订单时商品的售卖价
     */
    private BigDecimal salePrice;
    /**
     * 扣除优惠后单件sku的均摊价格
     */
    private BigDecimal realPrice;
    /**
     * 商品名称
     */
    private String title;
    /**
     * 头图
     */
    private String headImg;
}
