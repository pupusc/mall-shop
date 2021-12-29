package com.fangdeng.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DeliveryItemVO implements Serializable {
    private static final long serialVersionUID = 1437750054116565317L;
    @ApiModelProperty(value = "商品名称")
    private String itemName;

    @ApiModelProperty(value = "SKU名称")
    private String skuName;

    @ApiModelProperty(value = "SKU编码")
    private String skuCode;

    @ApiModelProperty(value = "SPU编码")
    private String itemCode;

    @ApiModelProperty(value = "sku")
    private String skuId;

    @ApiModelProperty(value = "发货数量")
    private Long qty;

    @ApiModelProperty(value = "子订单")
    private String oid;
}
