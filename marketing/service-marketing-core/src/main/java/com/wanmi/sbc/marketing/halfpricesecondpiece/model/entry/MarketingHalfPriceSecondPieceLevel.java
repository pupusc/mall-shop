package com.wanmi.sbc.marketing.halfpricesecondpiece.model.entry;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 第二件半价
 */
@Entity
@Table(name = "marketing_half_price_second_piece")
@Data
public class MarketingHalfPriceSecondPieceLevel {

    /**
     *  第二件半价规则Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 第二件半价营销Id
     */
    @Column(name = "marketing_id")
    private Long marketingId;

    /**
     *  第二件半价件数
     */
    @Column(name = "number")
    private Long number;

    /**
     *  第二件半价折数
     */
    @Column(name = "discount")
    private BigDecimal discount;

}
