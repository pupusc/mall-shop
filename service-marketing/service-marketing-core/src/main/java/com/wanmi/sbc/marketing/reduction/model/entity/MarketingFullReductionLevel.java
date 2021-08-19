package com.wanmi.sbc.marketing.reduction.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 满减
 */
@Entity
@Table(name = "marketing_full_reduction_level")
@Data
public class MarketingFullReductionLevel {

    /**
     *  满减级别Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reduction_level_id")
    private Long reductionLevelId;

    /**
     *  满赠Id
     */
    @Column(name = "marketing_id")
    private Long marketingId;

    /**
     *  满金额
     */
    @Column(name = "full_amount")
    private BigDecimal fullAmount;

    /**
     *  满数量
     */
    @Column(name = "full_count")
    private Long fullCount;

    /**
     *  满金额|数量后减多少元
     */
    @Column(name = "reduction")
    private BigDecimal reduction;

}
