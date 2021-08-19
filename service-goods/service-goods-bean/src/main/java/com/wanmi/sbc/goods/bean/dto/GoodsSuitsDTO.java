package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
public class GoodsSuitsDTO implements Serializable {

    private static final long serialVersionUID = -8807674093765870168L;

    /**
     * 商品skuid
     */
    @ApiModelProperty(value = "商品skuid")
    @NotNull
    private String goodsInfoId;

    /**
     * 折扣价 必填项
     */
    @ApiModelProperty(value = "折扣价 必填项")
    @NotNull
    private BigDecimal discountPrice;


    /**
     * 数量
     */
    @ApiModelProperty(value = "数量,必填")
    @NotNull
    @Max(999)
    private Long goodsSkuNum;



}
