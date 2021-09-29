package com.fangdeng.server.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeItemDTO {

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


}
