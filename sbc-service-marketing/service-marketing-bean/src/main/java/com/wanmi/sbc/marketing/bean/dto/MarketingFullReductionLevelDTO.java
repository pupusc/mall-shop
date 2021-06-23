package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>营销满减多级优惠实体</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
public class MarketingFullReductionLevelDTO implements Serializable {

    private static final long serialVersionUID = 512587750245287500L;

    /**
     *  满减级别Id
     */
    @ApiModelProperty(value = "满减级别主键Id")
    private Long reductionLevelId;

    /**
     *  满减Id
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
