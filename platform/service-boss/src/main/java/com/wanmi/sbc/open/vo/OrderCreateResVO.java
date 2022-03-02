package com.wanmi.sbc.open.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liang Jun
 * @date 2022-02-15 18:02:00
 */
@Data
public class OrderCreateResVO {
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 支付金额
     */
    private BigDecimal totalPrice;
    /**
     * 原始价格
     */
    private BigDecimal originPrice;
}
