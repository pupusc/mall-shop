package com.wanmi.sbc.marketing.discount.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 积分换购营销
 */
@Data
@Entity
@Table(name = "marketing_point_buy_level")
public class MarketingPointBuyLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 营销id
     */
    @Column(name = "marketing_id")
    private Long marketingId;

    /**
     * 金额
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 积分
     */
    @Column(name = "point")
    private Integer point;
}
