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
    /**
     * 提示文案
     */
    private String tipText;
    /**
     * 已经满足的营销活动
     */
    private List<TradeMkt> tradeMkts = new ArrayList<>();

    /**
     * 最大可用积分
     */
    private String maxAvailablePoint;

    @Data
    public static class TradeMkt {
        private Long mktId;
        private Long mktLevelId;
    }
}
