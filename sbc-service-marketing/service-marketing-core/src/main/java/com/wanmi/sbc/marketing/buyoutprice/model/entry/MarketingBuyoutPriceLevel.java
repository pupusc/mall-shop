package com.wanmi.sbc.marketing.buyoutprice.model.entry;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 一口价
 */
@Entity
@Table(name = "marketing_buyout_price_level")
@Data
public class MarketingBuyoutPriceLevel {

    /**
     *  一口价规则Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reduction_level_id")
    private Long reductionLevelId;

    /**
     *  一口价营销Id
     */
    @Column(name = "marketing_id")
    private Long marketingId;

    /**
     *  一口价满金额
     */
    @Column(name = "full_amount")
    private BigDecimal fullAmount;

    /**
     *  一口价任选数量
     */
    @Column(name = "choice_count")
    private Long choiceCount;

}
