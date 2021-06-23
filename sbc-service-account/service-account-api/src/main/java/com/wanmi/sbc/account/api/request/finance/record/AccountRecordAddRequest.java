package com.wanmi.sbc.account.api.request.finance.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayWay;
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
 * <p>对账单结构</p>
 * Created by daiyitian on 2018-10-26-下午4:02.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRecordAddRequest implements Serializable {

    private static final long serialVersionUID = 5985244198595834106L;

    @ApiModelProperty(value = "对账单id")
    private String id;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long supplierId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private PayWay payWay;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Long points;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
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
    private String orderCode;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    private String returnOrderCode;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTime;

    /**
     * 支付/退款时间
     */
    @ApiModelProperty(value = "支付/退款时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tradeTime;

    /**
     * 交易类型，0：收入 1：退款
     */
    @ApiModelProperty(value = "交易类型", dataType = "com.wanmi.sbc.account.bean.enums.AccountRecordType")
    private Byte type;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discounts;

    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    private String tradeNo;
}
