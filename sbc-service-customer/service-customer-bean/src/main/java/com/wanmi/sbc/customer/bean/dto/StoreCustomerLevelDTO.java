package com.wanmi.sbc.customer.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺会员等级-共用DTO
 */
@ApiModel
@Data
public class StoreCustomerLevelDTO implements Serializable {

    private static final long serialVersionUID = -8576070570255835867L;
    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    private Long storeId;

    /**
     * 商家标识
     */
    @ApiModelProperty(value = "商家标识")
    private Long companyInfoId;

    /**
     * 客户等级标识
     */
    @ApiModelProperty(value = "客户等级标识")
    @NotNull
    private Long customerLevelId;

    /**
     * 折扣率
     */
    @ApiModelProperty(value = "折扣率")
    @NotNull
    @DecimalMax(value="1.00")
    @DecimalMin(value="0.01")
    private BigDecimal discountRate;
}
