package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PointsExchangeActivityGoodsVO implements Serializable {

    @ApiModelProperty("sku编码")
    private String skuNo;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("规格名称")
    private String skuName;
    @ApiModelProperty("分类")
    private Integer cate;
    @ApiModelProperty("品牌")
    private String brand;
    @ApiModelProperty("定价")
    private BigDecimal price;
    @ApiModelProperty("现金")
    private BigDecimal cash;
    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

}
