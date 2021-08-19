package com.wanmi.sbc.customer.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 分销员佣金统计，实体类映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCommissionStatistics implements Serializable{

    /**
     * 佣金总额
     */
    private BigDecimal commissionTotal;

    /**
     * 分销佣金总额
     */
    private BigDecimal commission;

    /**
     * 邀新奖金总额
     */
    private BigDecimal rewardCash;

    /**
     * 未入账分销佣金总额
     */
    private BigDecimal commissionNotRecorded;

    /**
     * 未入账邀新奖金总额
     */
    private BigDecimal rewardCashNotRecorded;

    public BigDecimal getCommissionTotal() {
        return Objects.nonNull(commissionTotal) ? commissionTotal : BigDecimal.ZERO;
    }

    public BigDecimal getCommission() {
        return Objects.nonNull(commission) ? commission : BigDecimal.ZERO;
    }

    public BigDecimal getRewardCash() {
        return Objects.nonNull(rewardCash) ? rewardCash : BigDecimal.ZERO;
    }

    public BigDecimal getCommissionNotRecorded() {
        return Objects.nonNull(commissionNotRecorded) ? commissionNotRecorded : BigDecimal.ZERO;
    }

    public BigDecimal getRewardCashNotRecorded() {
        return Objects.nonNull(rewardCashNotRecorded) ? rewardCashNotRecorded : BigDecimal.ZERO;
    }
}
