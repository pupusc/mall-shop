package com.wanmi.sbc.order.api.response.payorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class SumPayOrderPriceResponse implements Serializable {

    @ApiModelProperty(value = "合计收款金额")
    BigDecimal value;
}
