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
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// * @program: sbc-background
// * @description: ERP商品SPU对象
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-27 09:58
// **/
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ERPGoods implements Serializable {
//
//    /**
//     * 商品D
//     */
//    @JsonProperty("id")
//    private String id;
//
//    /**
//     * 商品SPU代码
//     */
//    @JsonProperty("code")
//    private String code;
//
//    /**
//     * 商品名称
//     */
//    @JsonProperty("name")
//    private String name;
//
//
//    /**
//     * 库存状态
//     */
//    @JsonProperty("stock_status_code")
//    private String stockStatusCode;
//
//    /**
//     * 成本价
//     */
//    @JsonProperty("cost_price")
//    private BigDecimal costPrice;
//
//    /**
//     * skus列表
//     */
//    @JsonProperty("name")
//    private List<ERPGoodsInfo> skus;
//
//    /**
//     * 是否已停用
//     */
//    @JsonProperty("del")
//    private boolean del;
//
//}
