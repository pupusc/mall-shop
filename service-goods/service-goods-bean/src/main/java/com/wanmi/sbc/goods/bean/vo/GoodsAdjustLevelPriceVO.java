package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 商品批量改价等级价格实体
 * Created by dyt on 2017/4/17.
 */
@ApiModel
@Data
public class GoodsAdjustLevelPriceVO implements Serializable {

    private static final long serialVersionUID = 8913909876435836589L;

    /**
     * 级别价格ID
     */
    @ApiModelProperty(value = "级别价格ID")
    private Long levelPriceId;

    /**
     * 等级ID
     */
    @ApiModelProperty(value = "等级ID")
    private Long levelId;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;

    /**
     * 等级价
     */
    @ApiModelProperty(value = "等级价")
    private BigDecimal price;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private String goodsId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private String goodsInfoId;
}
