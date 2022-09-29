//package com.wanmi.sbc.erp.entity;
//
///**
// * @program: sbc-background
// * @description: ERP退货单商品对象
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-28 11:33
// **/
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
// * @description: ERP退货单信息
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-28 11:21
// **/
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ERPReturnTradeItem implements Serializable {
//
//    /**
//     * 商品条码(商品条码与商品代码二者必选其一)
//     */
//    @JsonProperty("barcode")
//    private String barcode;
//
//    /**
//     *商品代码(商品条码与商品代码二者必选其一)
//     */
//    @JsonProperty("item_code")
//    private String itemCode;
//
//    /**
//     *规格代码(带规格的商品此字段必填)
//     */
//    @JsonProperty("sku_code")
//    private String skuCode;
//
//    /**
//     * 数量
//     */
//    @JsonProperty("qty")
//    private int qty;
//}
