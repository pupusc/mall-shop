package com.wanmi.sbc.returnorder.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 退款金额
 * Created by jinwei on 25/4/2017.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnPriceRequest {

    /**
     * 申请金额状态，是否启用
     */
    @ApiModelProperty(value = "申请金额状态，是否启用", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean applyStatus;

    /**
     * 申请金额
     */
    @ApiModelProperty(value = "申请金额")
    private BigDecimal applyPrice;

    /**
     * 退款总额
     */
    @ApiModelProperty(value = "退款总额")
    private BigDecimal totalPrice;
}
