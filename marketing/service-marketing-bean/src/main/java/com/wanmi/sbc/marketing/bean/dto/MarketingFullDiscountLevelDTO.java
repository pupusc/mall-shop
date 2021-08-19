package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>营销满折多级优惠实体类</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
public class MarketingFullDiscountLevelDTO implements Serializable {

    private static final long serialVersionUID = -8786652763834297585L;

    /**
     * 满折级别Id
     */
    @ApiModelProperty(value = "满折级别Id")
    private Long discountLevelId;

    /**
     * 满折ID
     */
    @ApiModelProperty(value = "营销ID")
    private Long marketingId;

    /**
     * 满金额
     */
    @ApiModelProperty(value = "满金额")
    private BigDecimal fullAmount;

    /**
     * 满数量
     */
    @ApiModelProperty(value = "满数量")
    private Long fullCount;

    /**
     * 满金额|数量后折扣
     */
    @ApiModelProperty(value = "满金额|数量后折扣")
    private BigDecimal discount;

}
