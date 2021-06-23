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
@AllArgsConstructor
@NoArgsConstructor
public class SuitsRelationGoodsInfoVO {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String  goodInfoId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsInfoName;


    /**
     *  商品图片 -默认主图
     */
    @ApiModelProperty(value = "商品图片 -默认主图")
    private String  mainImage;

    /**
     * 原价=市场价
     */
    @ApiModelProperty(value = "原价=市场价")
    private BigDecimal marketPrice;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String specDetail;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private  Long goodsInfoNum;



}
