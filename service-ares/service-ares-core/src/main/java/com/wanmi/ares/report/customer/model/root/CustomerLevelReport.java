package com.wanmi.ares.report.customer.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <p>客户等级维度统计视图</p>
 * Created by of628-wenzhi on 2017-09-27-下午6:58.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLevelReport extends CustomerBaseReport{

    private static final long serialVersionUID = -3363418514062030775L;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 客单价
     */
    private BigDecimal userPerPrice;
}
