package com.wanmi.sbc.order.api.response.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: wanggang
 * @createDate: 2018/12/5 15:41
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RefundOrderGetSumReturnPriceResponse implements Serializable {
    private static final long serialVersionUID = -2088880178169556305L;

    @ApiModelProperty(value = "合计退款金额")
    private BigDecimal result;
}
