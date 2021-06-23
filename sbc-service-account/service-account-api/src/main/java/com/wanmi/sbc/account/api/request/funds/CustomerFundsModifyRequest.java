package com.wanmi.sbc.account.api.request.funds;

import com.wanmi.sbc.account.bean.enums.WithdrawAmountStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金-提现金额更新对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class CustomerFundsModifyRequest implements Serializable {

    /**
     * 会员资金ID
     */
    @ApiModelProperty(value = "会员资金ID")
    private String customerFundsId;

    /**
     * 提现金额
     */
    @ApiModelProperty(value = "提现金额")
    private BigDecimal withdrawAmount;

    /**
     * 提现状态
     */
    @ApiModelProperty(value = "提现状态")
    private WithdrawAmountStatus withdrawAmountStatus;

    /**
     * 消费金额
     */
    @ApiModelProperty(value = "使用金额")
    private BigDecimal expenseAmount;
}
