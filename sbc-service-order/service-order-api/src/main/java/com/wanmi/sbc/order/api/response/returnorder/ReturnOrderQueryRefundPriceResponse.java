package com.wanmi.sbc.order.api.response.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退单查询可退金额请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderQueryRefundPriceResponse implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 可退金额
     */
    @ApiModelProperty(value = "可退金额")
    private BigDecimal refundPrice;
}
