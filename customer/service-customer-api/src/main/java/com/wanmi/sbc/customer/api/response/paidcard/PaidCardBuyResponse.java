package com.wanmi.sbc.customer.api.response.paidcard;

import com.wanmi.sbc.customer.bean.enums.PayFlagEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaidCardBuyResponse {

    @ApiModelProperty("是否需要支付标识")
    private PayFlagEnum payFlag;
    @ApiModelProperty("支付金额")
    private BigDecimal price;
    @ApiModelProperty("支付金额")
    private String payCode;
}
