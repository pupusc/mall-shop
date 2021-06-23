package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.account.bean.enums.InvoiceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-03 10:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class OrderInvoiceDTO implements Serializable {

    /**
     * 订单开票ID
     */
    @ApiModelProperty(value = "订单开票ID")
    private String orderInvoiceId;

    /**
     * 发票类型
     */
    @ApiModelProperty(value = "发票类型")
    private InvoiceType invoiceType;

    /**
     * 开票项id
     */
    @ApiModelProperty(value = "开票项id")
    private String projectId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String customerId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 发票title
     */
    @ApiModelProperty(value = "发票title")
    private String invoiceTitle;

    /**
     * 发票地址
     */
    @ApiModelProperty(value = "发票地址")
    private String invoiceAddress;

    /**
     * 开票时间
     */
    @ApiModelProperty(value = "开票时间")
    private String invoiceTime;

    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String contacts;

    /**
     * 收货人联系号码
     */
    @ApiModelProperty(value = "收货人联系号码")
    private String phone;

    /**
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private String address;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId = 0L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId = 0L;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayerNumber;

    /**
     * 订单开票收货地址
     */
    @ApiModelProperty(value = "订单开票收货地址")
    private String addressInfoId;

}
