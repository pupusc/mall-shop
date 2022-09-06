//package com.wanmi.sbc.erp.entity;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.validation.constraints.Max;
//import java.io.Serializable;
//import java.util.List;
//
///**
// * @program: sbc-background
// * @description: ERP发货信息
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-27 19:09
// **/
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ERPDelivery implements Serializable {
//
//    /**
//     * 发货单编号
//     */
//    private String code;
//
//    /**
//     * 平台单号
//     */
//    @JsonProperty("platform_code")
//    private String platformCode;
//
//    /**
//     * 发货单状态对象
//     */
//    @JsonProperty("delivery_statusInfo")
//    private ERPDeliveryStatusInfo deliveryStatusInfo;
//
//    /**
//     * 发货单商品集合
//     */
//    @JsonProperty("details")
//    private List<ERPDeliveryItems> details;
//
//    /**
//     * 收货人名称
//     */
//    @JsonProperty("receiver_name")
//    private String receiverName;
//
//    /**
//     * 收货人电话
//     */
//    @JsonProperty("receiver_phone")
//    private String receiverPhone;
//
//    /**
//     * 收获人手机号
//     */
//    @JsonProperty("receiver_mobile")
//    private String receiverMobile;
//
//    /**
//     * 收货地址
//     */
//    @JsonProperty("receiver_address")
//    private String receiverAddress;
//
//    /**
//     * 收货地区
//     */
//    @JsonProperty("area_name")
//    private String areaName;
//
//    /**
//     * 快递公司代码
//     */
//    @JsonProperty("express_code")
//    private String expressCode;
//
//    /**
//     * 快递公司名称
//     */
//    @JsonProperty("express_name")
//    private String expressName;
//
//    /**
//     * 快递单号
//     */
//    @JsonProperty("express_no")
//    private String expressNo;
//
//    /**
//     * 子订单号
//     */
//    @JsonProperty("oid")
//    private String oid;
//}
