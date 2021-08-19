package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>第二件半价规则</p>
 * author: weiwenhao
 * Date: 2020-05-22
 */
@ApiModel
@Data
public class HalfPriceSecondPieceLevelDTO implements Serializable {

    private static final long serialVersionUID = -6209874995946285771L;
    /**
     *  第二件班级规则id
     */
    @ApiModelProperty(value = "第二件班级规则id")
    private Long id;

    /**
     *  第二件半价营销ID
     */
    @ApiModelProperty(value = "第二件半价营销ID")
    private Long marketingId;

    /**
     * 件数
     */
    @ApiModelProperty(value = "件数")
    private Long number;


    /**
     *折扣数
     */
    @ApiModelProperty(value = "折扣数")
    private BigDecimal discount;

}
