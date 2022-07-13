package com.soybean.mall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-21 11:23:00
 */
@Data
public class CalcPriceSum {
    /**
     * 商品总额
     */
    private BigDecimal totalPrice = BigDecimal.ZERO;
    /**
     * 商品总额明细
     */
    private List<CalcPriceItem> totalPriceItems = new ArrayList<>();
    /**
     * 优惠总额（discountsTotalPrice）
     */
    private BigDecimal cutPrice = BigDecimal.ZERO;
    /**
     * 优惠金额明细
     */
    private List<CalcPriceItem> cutPriceItems;
    /**
     * 订单总额（totalPrice）
     */
    private BigDecimal payPrice = BigDecimal.ZERO;
}
