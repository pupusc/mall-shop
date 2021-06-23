package com.wanmi.sbc.order.thirdplatformtrade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * linkedmall 物流商品
 *
 * @author yuhuiyu
 * Date 2020-8-22 13:01:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkedMallGoods {

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * linkedmall 商品id
     */
    private String itemId;
}
