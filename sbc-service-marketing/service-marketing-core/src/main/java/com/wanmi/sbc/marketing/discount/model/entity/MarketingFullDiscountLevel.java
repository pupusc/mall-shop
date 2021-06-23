package com.wanmi.sbc.marketing.discount.model.entity;

import com.wanmi.sbc.marketing.common.BaseBean;
import com.wanmi.sbc.marketing.common.MarketingLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * 营销满折多级优惠实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "marketing_full_discount_level")
public class MarketingFullDiscountLevel extends BaseBean implements MarketingLevel {

    private static final long serialVersionUID = -5331096539594003185L;
    /**
     * 满折级别Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_level_id")
    private Long discountLevelId;

    /**
     * 满折ID
     */
    @Column(name = "marketing_id")
    private Long marketingId;

    /**
     * 满金额
     */
    @Column(name = "full_amount")
    private BigDecimal fullAmount;

    /**
     * 满数量
     */
    @Column(name = "full_count")
    private Long fullCount;

    /**
     * 满金额|数量后折扣
     */
    @Column(name = "discount")
    private BigDecimal discount;
}
