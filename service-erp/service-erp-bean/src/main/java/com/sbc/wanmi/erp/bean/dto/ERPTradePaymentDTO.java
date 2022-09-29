//package com.sbc.wanmi.erp.bean.dto;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
//import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
///**
// * @program: sbc-background
// * @description: ERP订单支付明细DTO
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-29 16:49
// **/
//@Data
//@Builder
//@ApiModel
//@NoArgsConstructor
//@AllArgsConstructor
//public class ERPTradePaymentDTO implements Serializable {
//
//    /**
//     * 支付方式
//     */
//    @ApiModelProperty(value = "支付方式")
//    private String payTypeCode;
//
//    /**
//     * 支付金额
//     */
//    @ApiModelProperty(value = "支付金额")
//    private String payment;
//
//    /**
//     * 支付时间
//     */
//    @ApiModelProperty(value = "支付时间")
//    private long paytime;
//
//    /**
//     * 交易号
//     */
//    @ApiModelProperty(value = "交易号")
//    private String payCode;
//
//    /**
//     * 账号
//     */
//    @ApiModelProperty(value = "账号")
//    private String account;
//}
