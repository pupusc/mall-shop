package com.soybean.mall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @desc 购物车信息
 * @date 2022-06-15 11:20:00
 */
@Data
public class PurchasePriceResultVO {
    /**
     * 原始金额
     */
    private BigDecimal totalPrice = BigDecimal.ZERO;
    /**
     * 原始金额明细
     */
    private List<CalcPriceItem> totalPriceItems = new ArrayList<>();
    /**
     * 优惠金额
     */
    private BigDecimal cutPrice = BigDecimal.ZERO;
    /**
     * 优惠金额明细
     */
    private List<CalcPriceItem> cutPriceItems = new ArrayList<>();
    /**
     * 支付金额
     */
    private BigDecimal payPrice = BigDecimal.ZERO;
}
