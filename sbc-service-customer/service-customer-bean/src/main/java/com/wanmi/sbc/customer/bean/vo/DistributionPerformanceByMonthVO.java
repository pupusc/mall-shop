package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>分销业绩月统计记录</p>
 * Created by of628-wenzhi on 2019-04-17-16:01.
 */
@Data
public class DistributionPerformanceByMonthVO implements Serializable {

    private static final long serialVersionUID = 5605609078002688848L;

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

    /**
     * 年月（yyyy-MM）
     */
    @ApiModelProperty("年月（yyyy-MM）")
    private String targetMonth;

}
