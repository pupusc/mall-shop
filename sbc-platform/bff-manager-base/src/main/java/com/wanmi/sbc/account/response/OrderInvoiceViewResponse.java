package com.wanmi.sbc.account.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-17 10:43
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInvoiceViewResponse implements Serializable {

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String customerName;

    /**
     * 开票时间
     */
    @ApiModelProperty(value = "开票时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime invoiceTime;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderPrice;

    /**
     * 付款状态
     */
    @ApiModelProperty(value = "付款状态")
    private PayOrderStatus payOrderStatus;

    /**
     * 发票类型
     */
    @ApiModelProperty(value = "发票类型")
    private InvoiceType invoiceType;

    /**
     * 发票title
     */
    @ApiModelProperty(value = "发票title")
    private String invoiceTitle;

    /**
     * 纳税识别号
     */
    @ApiModelProperty(value = "纳税识别号")
    private String taxNo;

    /**
     * 注册地址
     */
    @ApiModelProperty(value = "注册地址")
    private String registerAddress;

    /**
     * 注册电话
     */
    @ApiModelProperty(value = "注册电话")
    private String registerPhone;

    /**
     * 银行账户号
     */
    @ApiModelProperty(value = "银行账户号")
    private String bankNo;

    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称")
    private String bankName;

    /**
     * 发票寄送地址
     */
    @ApiModelProperty(value = "发票寄送地址")
    private String invoiceAddress;

    /**
     * 开票项目
     */
    @ApiModelProperty(value = "开票项目")
    private String projectName;

    /**
     * 订单开票状态
     */
    @ApiModelProperty(value = "订单开票状态")
    private InvoiceState invoiceState;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;
}
