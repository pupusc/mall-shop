package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.dto.CompanyInfoDTO;
import com.wanmi.sbc.customer.bean.dto.CustomerDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账务支付单
 * Created by zhangjin on 2017/4/20.
 */
@Data
@ApiModel
public class PayOrderDTO implements Serializable{


    @ApiModelProperty(value = "支付单标识")
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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    @ApiModelProperty(value = "删除标识", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    @ApiModelProperty(value = "用户详情")
    private CustomerDetailDTO customerDetail;

    /**
     * 支付单金额
     */
    @ApiModelProperty(value = "支付单金额")
    private BigDecimal payOrderPrice;

    @ApiModelProperty(value = "收款单")
    @JsonManagedReference
    private ReceivableDTO receivable;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private PayType payType;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private Long companyInfoId;

    @ApiModelProperty(value = "公司信息")
    @Transient
    private CompanyInfoDTO companyInfo;
}
