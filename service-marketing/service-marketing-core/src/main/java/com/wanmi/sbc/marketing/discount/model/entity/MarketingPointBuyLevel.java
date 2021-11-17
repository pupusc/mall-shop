package com.wanmi.sbc.marketing.discount.model.entity;

import com.wanmi.sbc.marketing.common.BaseBean;
import com.wanmi.sbc.marketing.common.MarketingLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 积分换购营销
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "marketing_point_buy_level")
public class MarketingPointBuyLevel extends BaseBean {

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
    @Column(name = "money")
    private Double money;

    /**
     * 定价
     */
    private Double price;

    /**
     * 积分
     */
    @Column(name = "point_need")
    private Integer pointNeed;

    /**
     * 商品sku
     */
    private String skuId;
}
