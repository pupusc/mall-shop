package com.wanmi.sbc.marketing.bean.dto;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class MarketingPointBuyLevelDto {

    private Long id;
    /**
     * 金额
     */
    private Double money;

    /**
     * 积分
     */
    private Integer pointNeed;
}
