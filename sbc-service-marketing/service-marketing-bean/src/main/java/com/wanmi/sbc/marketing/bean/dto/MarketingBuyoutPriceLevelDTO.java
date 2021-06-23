package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>满赠</p>
 * author: weiwenhao
 * Date: 2020-04-13
 */
@ApiModel
@Data
public class MarketingBuyoutPriceLevelDTO implements Serializable {

    private static final long serialVersionUID = -6209874995946285771L;
    /**
     *  一口价Id
     */
    @ApiModelProperty(value = "打包级别Id")
    private Long reductionLevelId;

    /**
     *  一口价营销ID
     */
    @ApiModelProperty(value = "打包一口价营销ID")
    private Long marketingId;

    /**
     * 满金额
     */
    @ApiModelProperty(value = "满金额")
    private BigDecimal fullAmount;


    /**
     *任选数量
     */
    @ApiModelProperty(value = "任选数量")
    private Long choiceCount;

}
