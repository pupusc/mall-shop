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
// * @description: ERP退货单对象
// * @author: 0F3685-wugongjiang
// * @create: 2021-02-05 14:36
// **/
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class ERPReturnTrade implements Serializable {
//
//    /**
//     * 单据编号
//     */
//    @JsonProperty("code")
//    private String code;
//
//    /**
//     * 入库时间
//     */
//    @JsonProperty("receive_date")
//    private String receiveDate;
//
//    /**
//     * 平台单号
//     */
//    @JsonProperty("platform_code")
//    private String platformCode;
//
//    /**
//     * 主库状态(0:未入库
//     * 1:入库成功)
//     */
//    @JsonProperty("receive")
//    private int receive;
//}
