package com.wanmi.sbc.customer.api.response.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分销员佣金，汇总统计
 */
@ApiModel
@Data
@NoArgsConstructor
public class DistributionCommissionStatisticsResponse implements Serializable {

    /**
     * 佣金总额
     */
    @ApiModelProperty(value = "佣金总额")
    private BigDecimal commissionTotal;

    /**
     * 分销佣金总额
     */
    @ApiModelProperty(value = "分销佣金总额")
    private BigDecimal commission;

    /**
     * 邀新奖金总额
     */
    @ApiModelProperty(value = "邀新奖金总额")
    private BigDecimal rewardCash;

    /**
     * 未入账分销佣金总额
     */
    @ApiModelProperty(value = "未入账分销佣金总额")
    private BigDecimal commissionNotRecorded;

    /**
     * 未入账邀新奖金总额
     */
    @ApiModelProperty(value = "未入账邀新奖金总额")
    private BigDecimal rewardCashNotRecorded;

    public DistributionCommissionStatisticsResponse(BigDecimal commissionTotal,BigDecimal commission,
                                                    BigDecimal rewardCash,BigDecimal commissionNotRecorded,BigDecimal rewardCashNotRecorded){
        this.commissionTotal=commissionTotal;
        this.commission=commission;
        this.rewardCash=rewardCash;
        this.commissionNotRecorded=commissionNotRecorded;
        this.rewardCashNotRecorded=rewardCashNotRecorded;
    }
}
