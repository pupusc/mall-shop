package com.sbc.wanmi.erp.bean.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 * @description: ERP商品库存VO对象
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 18:03
 **/
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ERPGoodsInfoVO implements Serializable {

    /**
     * 是否停用
     */
    private Boolean del;

    /**
     * 商品代码
     */
    @ApiModelProperty(value = "商品代码")
    private String itemCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String itemName;

    /**
     * 商品规格代码
     */
    @ApiModelProperty(value = "商品规格代码")
    private String skuCode;

    /**
     * 商品规格代码
     */
    @ApiModelProperty(value = "商品规格名称")
    private String itemSkuName;

    /**
     * 商品条码
     */
    @ApiModelProperty(value = "商品条码")
    private String barcode;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private int qty;

    /**
     * 可销售数
     */
    @ApiModelProperty(value = "可销售数")
    private int salableQty;

    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    private BigDecimal costPrice;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 在途数
     */
    private int roadQty;

    /**
     * 库存状态
     */
    private String stockStatusCode;

}
