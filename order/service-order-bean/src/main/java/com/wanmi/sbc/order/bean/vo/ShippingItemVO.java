package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 发货清单
 * @author wumeng[OF2627]
 *         company qianmi.com
 *         Date 2017-04-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ShippingItemVO {
    /**
     * 清单编号
     */
    @ApiModelProperty(value = "清单编号")
    private String listNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String itemName;

    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    private String bn;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量")
    @NotNull
    @Min(1L)
    private Long itemNum;

    /**
     * 商品单号
     */
    @ApiModelProperty(value = "商品单号")
    private String oid;

    @ApiModelProperty(value = "spuId")
    private String spuId;

    @ApiModelProperty(value = "skuId")
    @NotNull
    private String skuId;

    @ApiModelProperty(value = "skuNo")
    private String skuNo;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String pic;

    /**
     * 规格描述信息
     */
    @ApiModelProperty(value = "规格描述信息")
    private String specDetails;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "购买价格")
    private BigDecimal price;

    @ApiModelProperty(value = "积分价")
    private Long buyPoint;

    /**
     * 供应商商品编码
     */
    @ApiModelProperty(value = "供应商商品编码")
    private String providerSkuNo;

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Long points;

    /**
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型")
    private Integer goodsType;

}
