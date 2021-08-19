package com.wanmi.sbc.goods.bean.vo;


import com.wanmi.sbc.goods.bean.enums.PriceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品客户价格实体
 * Created by dyt on 2017/4/17.
 */
@ApiModel
@Data
public class GoodsCustomerPriceVO implements Serializable {

    private static final long serialVersionUID = 8700215685573926713L;

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
    @ApiModelProperty(value = "类型")
    @Enumerated
    private PriceType type;


    /**
     * 折扣 0-100
     */
    @ApiModelProperty(value = "企业购 折扣")
    private Integer discount;

    /**
     * 企业购 客户级别名称
     */
    @ApiModelProperty(value = "企业购 客户级别名称")
    private String levelName;

    @ApiModelProperty(value = "企业购 客户名称")
    private String customerName;
}
