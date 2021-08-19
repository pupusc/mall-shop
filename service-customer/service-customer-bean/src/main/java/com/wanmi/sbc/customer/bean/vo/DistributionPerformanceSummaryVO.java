package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>分销员指定日期范围内业绩汇总数据</p>
 * Created by of628-wenzhi on 2019-04-17-16:01.
 */
@Data
public class DistributionPerformanceSummaryVO implements Serializable {


    private static final long serialVersionUID = -6008072102814602633L;

    /**
     * 分销员id
     */
    @ApiModelProperty("分销员id")
    private String distributionId;

    /**
     * 会员id
     */
    @ApiModelProperty("会员id")
    private String customerId;

    /**
     * 销售额
     */
    @ApiModelProperty("销售额")
    private BigDecimal saleAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);

    /**
     * 预估收益
     */
    @ApiModelProperty("预估收益")
    private BigDecimal commission = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);

}
