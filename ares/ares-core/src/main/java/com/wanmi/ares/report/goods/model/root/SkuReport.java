package com.wanmi.ares.report.goods.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品日报表信息
 * Created by daiyitian on 2017/9/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuReport extends GoodsReport {

    /**
     * SKU独立访客的下单笔数
     */
    private Long customerCount = 0L;

    /**
     * SKU独立访客的付款笔数
     */
    private Long payCustomerCount = 0L;

    /**
     * SKU浏览量
     */
    private Long viewNum = 0L;

    private Long totalUv = 0L;

    /**
     * 单品转换率
     */
    private BigDecimal orderConversion = BigDecimal.ZERO;

    /**
     * 付款转换率
     */
    private BigDecimal payConversion = BigDecimal.ZERO;

    /**
     * 商品名称
     */
    private String name;

    /**
     * sku编码
     */
    private String skuNo;
}
