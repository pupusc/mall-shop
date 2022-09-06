//package com.wanmi.sbc.erp.request;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
///**
// * @program: sbc-background
// * @description: 订单拦截请求参数
// * @author: 0F3685-wugongjiang
// * @create: 2021-02-05 11:05
// **/
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@JsonInclude(value= JsonInclude.Include.NON_NULL)
//public class ERPTradeInterceptRequest extends ERPBaseRequest{
//
//    /**
//     * 商城订单号
//     */
//    @JsonProperty("platform_code")
//    private String platformCode;
//
//    /**
//     * 操作类型(1:拦截
//     * 2:取消拦截)
//     */
//    @JsonProperty("operate_type")
//    private int operateType;
//
//    /**
//     * 拦截类型代码(操作类型为1时必填，操作类型为2 时，此字段无效)
//     */
//    @JsonProperty("trade_hold_code")
//    private String tradeHoldCode;
//
//    /**
//     * 拦截原因
//     */
//    @JsonProperty("trade_hold_reason")
//    private String tradeHoldReason;
//}
