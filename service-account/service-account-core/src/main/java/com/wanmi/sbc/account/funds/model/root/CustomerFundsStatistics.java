package com.wanmi.sbc.account.funds.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 会员资金统计映射实体对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:42
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsStatistics implements Serializable{

    /**
     * 余额总额
     */
    private BigDecimal accountBalanceTotal;

    /**
     * 冻结余额总额
     */

    private BigDecimal blockedBalanceTotal;

    /**
     * 可提现金额总额
     */
    private BigDecimal withdrawAmountTotal;

    public BigDecimal getAccountBalanceTotal() {
        return Objects.nonNull(accountBalanceTotal) ? accountBalanceTotal : BigDecimal.ZERO ;
    }

    public BigDecimal getBlockedBalanceTotal() {
        return Objects.nonNull(blockedBalanceTotal) ? blockedBalanceTotal : BigDecimal.ZERO ;
    }

    public BigDecimal getWithdrawAmountTotal() {
        return Objects.nonNull(withdrawAmountTotal) ? withdrawAmountTotal : BigDecimal.ZERO ;
    }
}
