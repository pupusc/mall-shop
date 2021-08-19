package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>预售商品信息DTO</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSaleGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 预售id
     */
    @ApiModelProperty(value = "预售id")
    private Long bookingSaleId;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    private Long storeId;

    /**
     * skuID
     */
    @ApiModelProperty(value = "skuID")
    private String goodsInfoId;

    /**
     * spuID
     */
    @ApiModelProperty(value = "spuID")
    private String goodsId;

    /**
     * 定金
     */
    @ApiModelProperty(value = "定金")
    private BigDecimal handSelPrice;

    /**
     * 膨胀价格
     */
    @ApiModelProperty(value = "膨胀价格")
    private BigDecimal inflationPrice;

    /**
     * 预售价
     */
    @ApiModelProperty(value = "预售价")
    private BigDecimal bookingPrice;

    /**
     * 预售数量
     */
    @ApiModelProperty(value = "预售数量")
    private Integer bookingCount;

    /**
     * 定金支付数量
     */
    @ApiModelProperty(value = "定金支付数量")
    private Integer handSelCount;

    /**
     * 尾款支付数量
     */
    @ApiModelProperty(value = "尾款支付数量")
    private Integer tailCount;

    /**
     * 全款支付数量
     */
    @ApiModelProperty(value = "全款支付数量")
    private Integer payCount;

    /**
     * 预售库存
     */
    @ApiModelProperty(value = "预售库存")
    private Integer stock;

}