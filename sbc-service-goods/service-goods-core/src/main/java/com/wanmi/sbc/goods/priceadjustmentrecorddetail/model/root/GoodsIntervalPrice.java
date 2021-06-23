package com.wanmi.sbc.goods.priceadjustmentrecorddetail.model.root;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品批量改价阶梯价格实体
 * Created by dyt on 2017/4/17.
 */
@ApiModel
@Data
public class GoodsIntervalPrice implements Serializable {

    /**
     * 订货区间ID
     */
    @ApiModelProperty(value = "订货区间ID")
    private Long intervalPriceId;

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
}
