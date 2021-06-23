package com.wanmi.sbc.order.settlement.model.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.GatherType;
import com.wanmi.sbc.account.bean.enums.TradeType;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by hht on 2017/12/6.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettleTrade {
    /**
     * 订单支付时间 -- trade - tradeStatus - payTime
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tradePayTime;

    /**
     * 订单创建时间 -- trade - tradeStatus - createTime
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tradeCreateTime;

    /**
     * 订单完成时间 -- trade - tradeStatus - endTime
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tradeEndTime;

    /**
     * 订单入账时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime finalTime;

    /**
     * 订单编号 -- trade - id
     */
    private String tradeCode;

    /**
     * 订单类型 -- 普通
     */
    private TradeType tradeType;

    /**
     * 订单类型 0：普通订单；1：积分订单
     */
    @ApiModelProperty(value = "订单类型")
    private OrderType orderType;

    /**
     * 收款方 -- 平台
     */
    private GatherType gatherType;

    /**
     * 运费 -- trade - tradePrice - deliveryPrice
     */
    private BigDecimal deliveryPrice;

    /**
     * 实际退款金额
     */
    private BigDecimal returnPrice;

    /**
     * 订单交易总额
     */
    private BigDecimal salePrice;

    /**
     * 退单改价差额
     */
    private BigDecimal returnSpecialPrice;

    /**
     * 店铺应收金额
     */
    private BigDecimal storePrice;

    /**
     * 商品供货总额
     */
    private BigDecimal providerPrice;
}
