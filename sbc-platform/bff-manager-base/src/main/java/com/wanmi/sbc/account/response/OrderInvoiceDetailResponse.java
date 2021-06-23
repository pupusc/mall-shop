package com.wanmi.sbc.account.response;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
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
 * 订单开票详情response
 * Created by CHENLI on 2017/5/8.
 */
@ApiModel
@Data
public class OrderInvoiceDetailResponse {

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;

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
     * 付款状态0:已付款 1.未付款 2.待确认
     */
    @ApiModelProperty(value = "付款状态", dataType = "com.wanmi.sbc.order.bean.enums.PayState")
    private String payState;

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
     * 单位全称
     */
    @ApiModelProperty(value = "单位全称")
    private String companyName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayerNumber;

    /**
     * 单位电话
     */
    @ApiModelProperty(value = "单位电话")
    private String companyPhone;

    /**
     * 单位地址
     */
    @ApiModelProperty(value = "单位地址")
    private String companyAddress;

    /**
     * 银行基本户号
     */
    @ApiModelProperty(value = "银行基本户号")
    private String bankNo;

    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    private String bankName;

    /**
     * 开票项目名称
     */
    @ApiModelProperty(value = "开票项目名称")
    private String projectName;

    /**
     * 开票时间
     */
    @ApiModelProperty(value = "开票时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 发票地址
     */
    @ApiModelProperty(value = "发票地址")
    private String invoiceAddress;

    /**
     * 增专资质id
     */
    @ApiModelProperty(value = "增专资质id")
    private Long customerInvoiceId;
}
