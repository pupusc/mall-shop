package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PointsExchangeSkuDTO implements Serializable {
    private Integer id;
    @ApiModelProperty("商品编码")
    private String skuNo;
    @ApiModelProperty("定价")
    private BigDecimal price;
    @ApiModelProperty("现金支付")
    private BigDecimal cash;
    @ApiModelProperty("积分")
    private Integer points;
}
