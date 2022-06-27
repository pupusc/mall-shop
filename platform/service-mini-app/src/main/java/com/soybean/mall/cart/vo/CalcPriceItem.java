package com.soybean.mall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liang Jun
 * @date 2022-06-26 21:22:00
 */
@Data
public class CalcPriceItem {
    /**
     * 类型
     */
    private Integer type;
    /**
     * 说明
     */
    private String desc;
    /**
     * 金额
     */
    private BigDecimal amount;
}
