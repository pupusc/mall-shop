package com.soybean.mall.cart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liang Jun
 * @date 2022-06-26 21:22:00
 */
@AllArgsConstructor
@Data
public class CalcPriceItem {
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 说明
     */
    private String desc;
    /**
     * 类型
     */
    private Integer type;
}

