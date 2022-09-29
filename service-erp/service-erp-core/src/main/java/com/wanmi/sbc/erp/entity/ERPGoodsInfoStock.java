//package com.wanmi.sbc.erp.entity;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
///**
// * @program: sbc-background
// * @description: ERP商品SKU库存数据
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-27 14:31
// **/
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ERPGoodsInfoStock implements Serializable {
//
//    /**
//     * 商品条码
//     */
//    @JsonProperty("barcode")
//    private String barcode;
//
//    /**
//     * 是否停用
//     */
//    @JsonProperty("del")
//    private boolean del;
//
//    /**
//     * 库存数量
//     */
//    @JsonProperty("qty")
//    private int qty;
//
//    /**
//     * 仓库代码
//     */
//    @JsonProperty("warehouse_code")
//    private String warehouseCode;
//
//    /**
//     * 商品代码
//     */
//    @JsonProperty("item_code")
//    private String itemCode;
//
//    /**
//     * 商品规格代码
//     */
//    @JsonProperty("sku_code")
//    private String skuCode;
//
//    /**
//     * 商品名称
//     */
//    @JsonProperty("item_name")
//    private String itemName;
//
//    /**
//     * 商品规格代码
//     */
//    @JsonProperty("item_sku_name")
//    private String itemSkuName;
//
//    /**
//     * 可销售数
//     */
//    @JsonProperty("salable_qty")
//    private int salableQty;
//
//    /**
//     * 在途数
//     */
//    @JsonProperty("road_qty")
//    private int roadQty;
//
//    /**
//     * 可配数
//     */
//    @JsonProperty("pick_qty")
//    private int pickQty;
//}
