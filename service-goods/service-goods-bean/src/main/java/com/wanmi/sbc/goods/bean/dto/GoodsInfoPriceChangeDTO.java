package com.wanmi.sbc.goods.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoPriceChangeDTO implements Serializable {
    private static final long serialVersionUID = -6685775991990256123L;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * skuNo
     */
    private String skuNo;

    private String goodsInfoId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 更新时间
     */
    private String time;
    /**
     * 原价格
     */
    private BigDecimal oldPrice;
    /**
     * 新价格
     */
    private BigDecimal newPrice;

    private BigDecimal marketPrice;
}
