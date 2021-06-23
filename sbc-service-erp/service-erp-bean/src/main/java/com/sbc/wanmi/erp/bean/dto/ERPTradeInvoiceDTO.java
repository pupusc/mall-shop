package com.sbc.wanmi.erp.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: ERP订单开票DTO
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-29 16:57
 **/
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class ERPTradeInvoiceDTO implements Serializable {

    /**
     *发票种类(1:增值普通发票
     * 2:增值专用发票)
     */
    @ApiModelProperty(value = "发票种类")
    private Long invoiceType;

    /**
     * 发票抬头类型(1:个人
     * 2:企业
     * 默认1)
     */
    @ApiModelProperty(value = "发票抬头类型")
    private int invoiceTitleType;

    /**
     * 发票类型(1:纸质发票
     * 2:电子发票
     * 默认1)
     */
    @ApiModelProperty(value = "发票类型")
    private int invoiceTypeName;

    /**
     * 发票抬头
     */
    @ApiModelProperty(value = "发票抬头")
    private String invoiceTitle;

    /**
     *发票内容
     */
    @ApiModelProperty(value = "发票内容")
    private String invoiceContent;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String invoiceTexPayerNumber;

    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    private String invoiceBankName;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String invoiceBankAccount;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String invoiceAddress;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String invoicePhone;

    /**
     * 发票金额
     */
    @ApiModelProperty(value = "invoice_amount")
    private String invoiceAmount;
}
