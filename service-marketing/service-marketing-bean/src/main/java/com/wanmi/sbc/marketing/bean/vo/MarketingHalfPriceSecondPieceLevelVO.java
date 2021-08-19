package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>第二件半价</p>
 * author: weiwenhao
 * Date: 2020-05-22
 */
@ApiModel
@Data
public class MarketingHalfPriceSecondPieceLevelVO implements Serializable {

    private static final long serialVersionUID = -3849339077854218161L;

    /**
     *  第二件半价规则Id
     */
    @ApiModelProperty(value = "打包级别Id")
    private Long id;

    /**
     *  第二件半价营销ID
     */
    @ApiModelProperty(value = "打包一口价营销ID")
    private Long marketingId;

    /**
     *  第二件半价件数
     */
    @ApiModelProperty(value = "number")
    private Long number;

    /**
     *  第二件半价折数
     */
    @ApiModelProperty(value = "discount")
    private BigDecimal discount;

}
