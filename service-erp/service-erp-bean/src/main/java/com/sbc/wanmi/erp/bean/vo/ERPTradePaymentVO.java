package com.sbc.wanmi.erp.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ERPTradePaymentVO implements Serializable {

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String payTypeCode;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private String payment;
}
