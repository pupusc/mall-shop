package com.soybean.mall.order.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoDTO implements Serializable {
    private static final long serialVersionUID = -5541313863809006427L;
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
