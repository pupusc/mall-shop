package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 满减
 */
@ApiModel
@Data
public class MarketingFullReductionLevelVO implements Serializable {

    private static final long serialVersionUID = -6549673559289837537L;

    /**
     *  满减级别Id
     */
    @ApiModelProperty(value = "满减级别主键Id")
    private Long reductionLevelId;

    /**
     *  满赠Id
     */
    @ApiModelProperty(value = "满减营销Id")
    private Long marketingId;

    /**
     *  满金额
     */
    @ApiModelProperty(value = "满金额")
    private BigDecimal fullAmount;

    /**
     *  满数量
     */
    @ApiModelProperty(value = "满数量")
    private Long fullCount;

    /**
     *  满金额|数量后减多少元
     */
    @ApiModelProperty(value = "满金额|数量后减多少元")
    private BigDecimal reduction;

}
