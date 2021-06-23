package com.wanmi.sbc.order.orderinvoice.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单视图响应
 * Created by zhangjin on 2017/5/25.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInvoiceViewResponse implements Serializable{

    /**
     * 用户姓名
     */
    private String customerName;

    /**
     * 开票时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime invoiceTime;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal orderPrice;

    /**
     * 付款状态
     */
    private PayOrderStatus payOrderStatus;

    /**
     * 发票类型
     */
    private InvoiceType invoiceType;

    /**
     * 发票title
     */
    private String invoiceTitle;

    /**
     * 纳税识别号
     */
    private String taxNo;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 注册电话
     */
    private String registerPhone;

    /**
     * 银行账户号
     */
    private String bankNo;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 发票寄送地址
     */
    private String invoiceAddress;

    /**
     * 开票项目
     */
    private String projectName;

    /**
     * 订单开票状态
     */
    private InvoiceState invoiceState;

    /**
     * 商家名称
     */
    private String supplierName;
}
