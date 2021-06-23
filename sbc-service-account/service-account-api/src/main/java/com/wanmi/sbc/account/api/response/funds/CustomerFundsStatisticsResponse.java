package com.wanmi.sbc.account.api.response.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金-统计汇总对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsStatisticsResponse implements Serializable {

    @ApiModelProperty(value = "余额总额")
    private BigDecimal accountBalanceTotal;

    @ApiModelProperty(value = "冻结余额总额")
    private BigDecimal blockedBalanceTotal;

    @ApiModelProperty(value = "可提现余额总额")
    private BigDecimal withdrawAmountTotal;

    /**
     * 已提现金额
     */
    @ApiModelProperty(value = "已提现金额")
    private BigDecimal alreadyDrawAmount;

    public CustomerFundsStatisticsResponse(BigDecimal accountBalanceTotal, BigDecimal blockedBalanceTotal, BigDecimal withdrawAmountTotal) {
        this.accountBalanceTotal = accountBalanceTotal;
        this.blockedBalanceTotal = blockedBalanceTotal;
        this.withdrawAmountTotal = withdrawAmountTotal;
    }
}
