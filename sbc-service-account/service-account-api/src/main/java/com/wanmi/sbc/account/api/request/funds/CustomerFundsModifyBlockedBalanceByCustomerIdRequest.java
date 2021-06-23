package com.wanmi.sbc.account.api.request.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金-更新冻结金额
 * @author chenyufei
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsModifyBlockedBalanceByCustomerIdRequest implements Serializable {

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;


    /**
     * 冻结金额
     */
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal blockedBalance;
}
