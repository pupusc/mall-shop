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
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 订单开票返回列表
 * Created by CHENLI on 2017/5/5.
 */
@ApiModel
@Data
public class OrderInvoiceResponse {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String orderInvoiceId;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderPrice;

    /**
     * 付款状态0:已收款 1.未收款 2.待确认
     */
    @ApiModelProperty(value = "付款状态")
    private PayOrderStatus payOrderStatus;

    /**
     * 发票类型 0普通发票 1增值税专用发票
     */
    @ApiModelProperty(value = "发票类型")
    private InvoiceType invoiceType;

    /**
     * 发票抬头
     */
    @ApiModelProperty(value = "发票抬头")
    private String invoiceTitle;

    /**
     * 开票状态 0待开票 1 已开票
     */
    @ApiModelProperty(value = "开票状态")
    private InvoiceState invoiceState;

    /**
     * 开票时间
     */
    @ApiModelProperty(value = "开票时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime invoiceTime;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;
}
