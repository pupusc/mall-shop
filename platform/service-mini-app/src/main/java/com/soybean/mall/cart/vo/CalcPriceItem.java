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
     * 增加类型：101:商品费用；102:配送费用
     * 减少类型：201:会员折扣；202:优惠券抵扣；203:营销促销；
     */
    private Integer type;
}

