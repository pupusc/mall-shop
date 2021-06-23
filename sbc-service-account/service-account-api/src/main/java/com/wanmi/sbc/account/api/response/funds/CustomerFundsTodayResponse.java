package com.wanmi.sbc.account.api.response.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFundsTodayResponse {

    /**
     * 收入金额
     */
    @ApiModelProperty(value = "收入金额")
    private BigDecimal receiptAmount;

    /**
     * 支出金额
     */
    @ApiModelProperty(value = "支出金额")
    private BigDecimal paymentAmount;
}
