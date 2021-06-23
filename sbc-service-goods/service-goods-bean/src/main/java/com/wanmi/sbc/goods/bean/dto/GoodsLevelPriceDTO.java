package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.goods.bean.enums.PriceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 商品级别价格实体
 * Created by dyt on 2017/4/17.
 */
@ApiModel
@Data
public class GoodsLevelPriceDTO implements Serializable {

    private static final long serialVersionUID = 8913909876435836589L;

    /**
     * 级别价格ID
     */
    @ApiModelProperty(value = "级别价格ID")
    private Long levelPriceId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

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
     * 订货价
     */
    @ApiModelProperty(value = "订货价")
    private BigDecimal price;

    /**
     * 起订量
     */
    @ApiModelProperty(value = "起订量")
    private Long count;

    /**
     * 限订量
     */
    @ApiModelProperty(value = "限订量")
    private Long maxCount;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private String goodsInfoId;


    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", notes = "0：spu数据 1sku数据")
    private PriceType type;
}
