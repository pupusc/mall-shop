//package com.wanmi.sbc.erp.request;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
///**
// * @program: sbc-background
// * @description: 管易云ERP商品查询接口入参
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-27 09:33
// **/
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@JsonInclude(value= JsonInclude.Include.NON_NULL)
//public class ERPGoodsQueryRequest extends ERPBaseRequest{
//
//    /**
//     * 页码(默认为1)
//     */
//    @JsonProperty("page_no")
//    private int pageNo = 1;
//
//    /**
//     * 每页大小
//     */
//    @JsonProperty("page_size")
//    private int pageSize = 10;
//
//
//    /**
//     * 商品SPU代码
//     */
//    @JsonProperty("code")
//    private String code;
//
//}
