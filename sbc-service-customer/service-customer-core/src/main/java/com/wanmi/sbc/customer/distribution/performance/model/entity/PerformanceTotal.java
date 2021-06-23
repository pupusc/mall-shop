package com.wanmi.sbc.customer.distribution.performance.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>分销业绩月度汇总实体类</p>
 * Created by of628-wenzhi on 2019-04-23-18:04.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceTotal implements Serializable {
    private static final long serialVersionUID = -828005073839150730L;
    /**
     * 月销售额汇总
     */
    private BigDecimal saleAmount;

    /**
     * 预估收益汇总
     */
    private BigDecimal commission;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 分销员id
     */
    private String distributionId;

}
