package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>订单成功提交的返回信息</p>
 * Created by of628-wenzhi on 2017-07-25-下午3:52.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeCommitResultVO {

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String tid;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    @ApiModelProperty(value = "父订单号")
    private String parentTid;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private TradeStateVO tradeState;

    /**
     * 订单支付顺序
     */
    @ApiModelProperty(value = "订单支付顺序")
    private PaymentOrder paymentOrder;

    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额")
    private BigDecimal price;

    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalPrice;

    /**
     * 订单取消时间
     */
    @ApiModelProperty(value = "订单取消时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTimeOut;

    /**
     * 是否是预售商品
     */
    @ApiModelProperty(value = "是否是预售商品")
    private Boolean isBookingSaleGoods;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 是否平台自营
     */
    @ApiModelProperty(value = "是否平台自营",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean isSelf;

    /**
     * 交易金额
     */
    private BigDecimal originPrice;

    /**
     * 下发优惠券标记
     */
    private Boolean couponFlag;

    public TradeCommitResultVO(String tid, String parentTid, TradeStateVO tradeState, PaymentOrder paymentOrder, BigDecimal price, BigDecimal totalPrice,
                               LocalDateTime orderTimeOut, Boolean isBookingSaleGoods, String storeName, Boolean isSelf) {
        this.tid = tid;
        this.parentTid = parentTid;
        this.tradeState = tradeState;
        this.paymentOrder = paymentOrder;
        this.price = price;
        this.totalPrice = totalPrice;
        this.orderTimeOut = orderTimeOut;
        this.isBookingSaleGoods = isBookingSaleGoods;
        this.storeName = storeName;
        this.isSelf = isSelf;
    }
}
