//package com.wanmi.sbc.erp.entity;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
///**
// * @program: sbc-background
// * @description: 发货单商品列表
// * @author: 0F3685-wugongjiang
// * @create: 2021-02-18 18:03
// **/
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ERPDeliveryItems  implements Serializable {
//
//    /**
//     * SPU商品名称
//     */
//    @JsonProperty("item_name")
//    private String itemName;
//
//    /**
//     * SKU名称
//     */
//    @JsonProperty("sku_name")
//    private String skuName;
//
//    /**
//     * SKU编码
//     */
//    @JsonProperty("sku_code")
//    private String skuCode;
//
//    /**
//     * SPU编码
//     */
//    @JsonProperty("item_code")
//    private String itemCode;
//
//    /**
//     * 发货商品数量
//     */
//    @JsonProperty("qty")
//    private Integer qty;
//
//    /**
//     * 发货商品数量
//     */
//    @JsonProperty("oid")
//    private String oid;
//}
