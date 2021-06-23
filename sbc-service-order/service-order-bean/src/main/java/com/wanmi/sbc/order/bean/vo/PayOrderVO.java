package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel
public class PayOrderVO implements Serializable {

    @ApiModelProperty(value = "付款单id")
    private String payOrderId;

    /**
     * 支付单号
     */
    @ApiModelProperty(value = "支付单号")
    private String payOrderNo;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerDetailId;

    /**
     * 支付单状态
     */
    @ApiModelProperty(value = "支付单状态")
    private PayOrderStatus payOrderStatus;

    @ApiModelProperty(value = "创建时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    @ApiModelProperty(value = "删除标记")
    private DeleteFlag delFlag;

    @ApiModelProperty(value = "客户信息")
    private CustomerDetailVO customerDetail;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payOrderPrice;

    @ApiModelProperty(value = "收款单")
    @JsonManagedReference
    private ReceivableVO receivable;

    @ApiModelProperty(value = "支付类型")
    private PayType payType;

    @ApiModelProperty(value = "公司信息id")
    private Long companyInfoId;

    @ApiModelProperty(value = "公司信息")
    private CompanyInfoVO companyInfo;
}

