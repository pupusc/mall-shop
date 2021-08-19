package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : baijz
 * @Date : 2019/2/26 14 14
 * @Description : 分销记录使用到的货品信息
 */
@ApiModel
@Data
public class GoodsInfoForDistribution implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品SKU编号")
    private String goodsInfoId;

    @ApiModelProperty("商品编号")
    private String goodsId;

    @ApiModelProperty("商品SKU名称")
    private String goodsInfoName;

    @ApiModelProperty("商品SKU编码")
    private String goodsInfoNo;

    @ApiModelProperty("商品图片")
    private String goodsInfoImg;

    @ApiModelProperty("商品条形码")
    private String goodsInfoBarcode;

    @ApiModelProperty(
            value = "规格名称规格值",
            example = "颜色:红色;大小:16G"
    )
    private String specText;

    @ApiModelProperty("分销佣金")
    private BigDecimal distributionCommission;
}
