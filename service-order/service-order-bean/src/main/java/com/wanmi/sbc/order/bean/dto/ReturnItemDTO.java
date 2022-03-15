package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退货商品类目
 * Created by jinwei on 19/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ReturnItemDTO implements Serializable {

    private static final long serialVersionUID = -6299234989806779463L;

    @ApiModelProperty(value = "skuId")
    private String skuId;

    @ApiModelProperty(value = "sku 名称")
    private String skuName;

    @ApiModelProperty(value = "sku 编号")
    private String skuNo;

    /**
     * 规格信息
     */
    @ApiModelProperty(value = "规格信息")
    private String specDetails;

    /**
     * 退货商品单价 = 商品原单价 - 商品优惠单价
     */
    @ApiModelProperty(value = "退货商品单价 = 商品原单价 - 商品优惠单价")
    private BigDecimal price;

    /**
     * 平摊价格
     */
    @ApiModelProperty(value = "平摊价格")
    private BigDecimal splitPrice;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;

    /**
     * 供货价小计
     */
    @ApiModelProperty(value = "供货价小计")
    private BigDecimal providerPrice;

    /**
     * 订单平摊价格
     */
    @ApiModelProperty(value = "订单平摊价格")
    private BigDecimal orderSplitPrice;

    /**
     * 申请退货数量
     */
    @ApiModelProperty(value = "申请退货数量")
    private Integer num;

    /**
     * 退货商品图片路径
     */
    @ApiModelProperty(value = "退货商品图片路径")
    private String pic;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;
    /**
     * 商品类型
     */
    private GoodsType goodsType;
    /**
     * 仍可退数量
     */
    @ApiModelProperty(value = "仍可退数量")
    private Integer canReturnNum;

    /**
     * 购买积分，被用于普通订单的积分+金额混合商品
     */
    @ApiModelProperty(value = "购买积分")
    private Long buyPoint;



    /**
     * 第三方平台的spuId
     */
    private String thirdPlatformSpuId;

    /**
     * 第三方平台的skuId
     */
    private String thirdPlatformSkuId;

    /**
     * 商品来源，0供应商，1商家 2linkedMall
     */
    private Integer goodsSource;

    /**
     *第三方平台类型，0，linkedmall
     */
    private ThirdPlatformType thirdPlatformType;


    /**
     * 第三方平台-明细子订单id
     */
    private String thirdPlatformSubOrderId;

    /**
     * 供货商id
     */
    private Long providerId;

    /**
     * 应退积分
     */
    @ApiModelProperty(value = "应退积分")
    private Long splitPoint;


    private BigDecimal applyRealPrice;

    private Long applyKnowledge;

    private Long applyPoint;
}
