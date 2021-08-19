package com.wanmi.sbc.marketing.bean.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsGoodsInfoDetailVO {
    private static final long serialVersionUID = 1L;

    /**
     * 营销活动Id
     */
    @ApiModelProperty(value = "营销活动Id")
    private Long marketingId;

    /**
     * 组合套餐名称
     */
    @ApiModelProperty(value = "组合套餐名称")
    private String marketingName;

    /**
     * 套餐主图
     */
    @ApiModelProperty(value = "套餐主图")
    private String mainImage;

    /**
     * 套餐价
     */
    @ApiModelProperty(value = "套餐价")
    private BigDecimal suitsPrice;

    /**
     * 组合套餐商品最高省
     */
    @ApiModelProperty(value = "组合套餐商品最高省")
    private BigDecimal suitsNoNeedPrice;

}
