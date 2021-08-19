package com.wanmi.ares.report.customer.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 客户等级维度统计视图
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAreaReport extends CustomerBaseReport{
    private static final long serialVersionUID = -3363418514062030788L;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 客单价
     */
    private BigDecimal userPerPrice;

    /**
     * 市区id
     */
    private String cityId;
}
