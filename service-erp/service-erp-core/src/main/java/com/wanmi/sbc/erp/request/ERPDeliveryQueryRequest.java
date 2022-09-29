//package com.wanmi.sbc.erp.request;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.*;
//
///**
// * @program: sbc-background
// * @description: ERP发货单查询接口
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-27 18:53
// **/
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@JsonInclude(value= JsonInclude.Include.NON_NULL)
//public class ERPDeliveryQueryRequest extends ERPBaseRequest{
//
//    /**
//     * 商城订单号
//     */
//    @JsonProperty("outer_code")
//    private String outerCode;
//
//    /**
//     * 发货状态 0:未发货、发货中、发货失败，1:发货成功
//     */
//    private Integer delivery;
//
//}
