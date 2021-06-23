package com.wanmi.sbc.goods.bean.vo;


import com.wanmi.sbc.goods.bean.enums.PriceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品订货区间价格实体
 *
 * @author dyt
 * @date 2017/4/17
 */
@ApiModel
@Data
public class GoodsIntervalPriceVO implements Serializable {

    private static final long serialVersionUID = 2762796550616761427L;

    /**
     * 订货区间ID
     */
    @ApiModelProperty(value = "订货区间ID")
    private Long intervalPriceId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private String goodsId;

    /**
     * 订货区间
     */
    @ApiModelProperty(value = "订货区间")
    private Long count;

    /**
     * 订货价
     */
    @ApiModelProperty(value = "订货价")
    private BigDecimal price;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private String goodsInfoId;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", notes = "0：spu数据 1sku数据 2:企业购SKU")
    private PriceType type;

    /**
     * 折扣 0-100
     */
    @ApiModelProperty(value = "折扣")
    private Integer discount;

    @ApiModelProperty(value = "可以删除")
    private Boolean canDel = true;
}
