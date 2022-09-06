//package com.wanmi.sbc.erp.entity;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
//import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
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
// * @description: ERP订单返货状态信息
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-27 19:04
// **/
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ERPDeliveryStatusInfo implements Serializable {
//
//    /**
//     * 发货状态(-1:发货失败 0:未发货 1:发货中 2:发货成功)
//     */
//    @JsonProperty("delivery")
//    private int delivery;
//
//    /**
//     * 预计发货时间
//     */
//    @JsonProperty("delivery_date")
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
//    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    private LocalDateTime deliveryDate;
//}
