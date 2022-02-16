package com.wanmi.sbc.open.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liang Jun
 * @date 2022-02-15 14:14:00
 */
@Data
public class GoodsListResVO {
    /**
     * 商品id
     */
    private String skuId;
    /**
     * 商品编码
     */
    private String skuNo;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 销售价
     */
    private BigDecimal salePrice;
    /**
     * 成本价
     */
    private BigDecimal costPrice;
    /**
     * 上下架状态：0未上架；1已上架；2部分上架；
     */
    private Integer shelfStatus;
}
