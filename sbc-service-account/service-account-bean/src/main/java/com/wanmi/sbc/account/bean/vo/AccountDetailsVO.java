package com.wanmi.sbc.account.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>对账明细返回数据结构</p>
 * Created by of628-wenzhi on 2017-12-08-下午5:06.
 */
@ApiModel
@Data
public class AccountDetailsVO implements Serializable {


    private static final long serialVersionUID = 1952054705815175624L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private PayWay payWay;

    /**
     * 金额,单位元，格式："￥#,###.00"
     */
    @ApiModelProperty(value = "金额")
    private String amount;

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
     * 下单/退单时间
     */
    @ApiModelProperty(value = "下单/退单时间")
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
     * 优惠金额，单位元，格式："￥#,###.00"
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discounts;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    private String returnOrderCode;

    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    private String tradeNo;
}
