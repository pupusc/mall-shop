package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import lombok.Data;

import javax.persistence.Column;

@ApiModel
@Data
public class PresellSaleGoodsVO {



    /**
     * 预售活动关联商品id
     */
    @ApiModelProperty(value = "预售活动关联商品id")
    private String presellSaleGoodsId;


    /**
     * 预售活动id
     */
    @ApiModelProperty(value = "预售活动id")
    private String presellSaleId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 商品skuID
     */
    @ApiModelProperty(value = "商品skuID")
    private String goodsInfoId;


    /**
     * 商品spuId
     */
    @ApiModelProperty(value = "商品spuId")
    private String goodsId;


    /**
     * 预售商品定金金额
     */
    @ApiModelProperty(value = "预售商品定金金额")
    private BigDecimal handselPrice;


    /**
     * 预售商品定金膨胀金额
     */
    @ApiModelProperty(value = "预售商品定金膨胀金额")
    private BigDecimal inflationPrice;


    /**
     * 预售商品限购数量
     */
    @ApiModelProperty(value = "预售商品限购数量")
    private Integer presellSaleCount;

    /**
     * 支付定金人数
     */
    @ApiModelProperty(value = "支付定金人数")
    private Integer handselNum;


    /**
     * 支付尾款人数
     */
    @ApiModelProperty(value = "支付尾款人数")
    private Integer finalPaymentNum;

    /**
     * 支付全款人数
     */
    @ApiModelProperty(value = "支付全款人数")
    private Integer fullPaymentNum;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String create_person;


    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String update_person;

    /**
     * 预售价格，只有预售类型为全款的有值
     */
    @ApiModelProperty(value = "预售价格")
    private BigDecimal presellPrice;
}
