package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退货总金额
 * Created by jinwei on 19/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ReturnPriceDTO implements Serializable {

    private static final long serialVersionUID = 8294753953899559151L;

    /**
     * 申请金额状态，是否启用
     */
    @ApiModelProperty(value = "申请金额状态，是否启用",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean applyStatus;

    /**
     * 申请金额
     */
    @ApiModelProperty(value = "申请金额")
    private BigDecimal applyPrice;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "商品总金额")
    private BigDecimal totalPrice;

    /**
     * 运费金额
     */
    private BigDecimal deliverPrice;

    /**
     * 实退金额，从退款流水中取的
     */
    @ApiModelProperty(value = "实退金额，从退款流水中取的")
    private BigDecimal actualReturnPrice;

    /**
     * 供货总额
     */
    @ApiModelProperty(value = "供货总额")
    private BigDecimal providerTotalPrice;



    /**
     * 应退定金
     */
    private BigDecimal earnestPrice;

    /**
     * 应退尾款
     */
    private BigDecimal tailPrice;

    /**
     * 是否是尾款申请
     */
    private Boolean isTailApply;

}
