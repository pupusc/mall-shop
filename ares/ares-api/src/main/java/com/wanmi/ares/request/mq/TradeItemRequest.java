package com.wanmi.ares.request.mq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单中的交易项(sku)
 * Author: bail
 * Time: 2017/10/18.18:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class TradeItemRequest extends GoodsInfoRequest{

    private static final long serialVersionUID = -797494478846583898L;

    /**
     * 销售价
     */
    private BigDecimal price;

    /**
     * 销售件数
     */
    private Long num;

}
