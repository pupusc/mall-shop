package com.wanmi.sbc.goods.bean.dto;


import com.wanmi.sbc.goods.bean.enums.PriceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品客户价格实体
 * Created by dyt on 2017/4/17.
 */
@ApiModel
@Data
public class GoodsCustomerPriceDTO implements Serializable {

    private static final long serialVersionUID = -6651281415953537335L;

    /**
     * 客户价格ID
     */
    @ApiModelProperty(value = "客户价格ID")
    private Long customerPriceId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private String goodsId;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

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
