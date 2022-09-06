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
// * @description: 订单开票信息
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-26 17:10
// **/
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class ERPTradeInvoice implements Serializable {
//
//    private static final long serialVersionUID = 1949128349043342299L;
//
//    /**
//     *发票种类(1:增值普通发票
//     * 2:增值专用发票)
//     */
//    @JsonProperty("invoice_type")
//    private Long invoiceType;
//
//    /**
//     * 发票抬头类型(1:个人
//     * 2:企业
//     * 默认1)
//     */
//    @JsonProperty("invoice_title_type")
//    private int invoiceTitleType;
//
//    /**
//     * 发票类型(1:纸质发票
//     * 2:电子发票
//     * 默认1)
//     */
//    @JsonProperty("invoice_type_name")
//    private int invoiceTypeName;
//
//    /**
//     * 发票抬头
//     */
//    @JsonProperty("invoice_title")
//    private String invoiceTitle;
//
//    /**
//     *发票内容
//     */
//    @JsonProperty("invoice_content")
//    private String invoiceContent;
//
//    /**
//     * 纳税人识别号
//     */
//    @JsonProperty("invoice_tex_payer_number")
//    private String invoiceTexPayerNumber;
//
//    /**
//     * 开户行
//     */
//    @JsonProperty("invoice_bank_name")
//    private String invoiceBankName;
//
//    /**
//     * 账号
//     */
//    @JsonProperty("invoice_bank_account")
//    private String invoiceBankAccount;
//
//    /**
//     * 地址
//     */
//    @JsonProperty("invoice_address")
//    private String invoiceAddress;
//
//    /**
//     * 电话
//     */
//    @JsonProperty("invoice_phone")
//    private String invoicePhone;
//
//    /**
//     * 发票金额
//     */
//    @JsonProperty("invoice_amount")
//    private String invoiceAmount;
//}
