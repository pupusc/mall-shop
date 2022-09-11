package com.sbc.wanmi.erp.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: sbc-background
 * @description: 订单商品DTO
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-29 16:48
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ERPTradeItemDTO implements Serializable {

    /**
     * 商品SPU代码
     */
    @ApiModelProperty(value = "商品SPU代码")
    private String itemCode;

    /**
     * 规格代码
     */
    @ApiModelProperty(value = "规格代码")
    private String skuCode;

    /**
     * 标准单价
     */
    @ApiModelProperty(value = "标准单价")
    private String originPrice;

    /**
     * 实际单价
     */
    @ApiModelProperty(value = "实际单价")
    private String price;

    /**
     *商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer qty;

    /**
     * 退款状态(0:未退款
     * 1:退款完成
     * 2:退款中)
     */
    @ApiModelProperty(value = "退款状态")
    private Integer refund;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;

    /**
     * 子订单ID
     */
    @ApiModelProperty(value = "子订单ID")
    private String oid;

    /**
     * 预计发货日期
     */
    @ApiModelProperty(value = "预计发货日期")
    private String planDeliveryDate;

    /**
     * 是否为预售
     */
    @ApiModelProperty(value = "presale")
    private boolean presale;

    /**
    成本价
     */
    private BigDecimal costPrice;

}
