package com.wanmi.sbc.marketing.gift.model.root;

import com.wanmi.sbc.marketing.bean.enums.GiftType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 满赠
 */
@Entity
@Table(name = "marketing_full_gift_level")
@Data
public class MarketingFullGiftLevel {

    /**
     *  满赠多级促销Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_level_id")
    private Long giftLevelId;

    /**
     *  满赠Id
     */
    @Column(name = "marketing_id")
    private Long marketingId;

    /**
     *  满金额赠
     */
    @Column(name = "full_amount")
    private BigDecimal fullAmount;

    /**
     *  满数量赠
     */
    @Column(name = "full_count")
    private Long fullCount;

    /**
     *  赠品赠送的方式 0:全赠  1：赠一个
     */
    @Column(name = "gift_type")
    @Enumerated
    private GiftType giftType;

    /**
     * 满赠赠品明细
     */
    @Transient
    private List<MarketingFullGiftDetail> fullGiftDetailList;
}
