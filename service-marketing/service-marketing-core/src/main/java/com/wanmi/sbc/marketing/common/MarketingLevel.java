package com.wanmi.sbc.marketing.common;

import java.math.BigDecimal;

/**
 * 数据库字段要求：
 * full_amount 满金额
 * full_count 满数量
 * <p>
 * 适用：满[减|折|赠]|购物赠券
 * <p>
 */
public interface MarketingLevel {

    BigDecimal getFullAmount();

    void setFullAmount(BigDecimal fullAMount);

    Long getFullCount();

    void setFullCount(Long fullCount);

//    BigDecimal getFullReduce();
//
//    void setFullReduce(BigDecimal fullReduce);
//
//    BigDecimal getDiscount();
//
//    void setFullReduce(BigDecimal discount);

}
