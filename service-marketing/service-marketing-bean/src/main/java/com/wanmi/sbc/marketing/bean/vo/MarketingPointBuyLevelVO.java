package com.wanmi.sbc.marketing.bean.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
public class MarketingPointBuyLevelVO {

    private Long id;

    /**
     * 营销id
     */
    private Long marketingId;

    /**
     * 金额
     */
    private Double money;

    /**
     * 积分
     */
    private Integer pointNeed;
}
