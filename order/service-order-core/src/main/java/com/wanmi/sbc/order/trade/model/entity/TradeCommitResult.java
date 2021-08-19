package com.wanmi.sbc.order.trade.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>订单成功提交的返回信息</p>
 * Created by of628-wenzhi on 2017-07-25-下午3:52.
 */
@Data
@AllArgsConstructor
public class TradeCommitResult {

    /**
     * 订单编号
     */
    private String tid;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    private String parentTid;

    /**
     * 订单状态
     */
    private TradeState tradeState;

    /**
     * 订单支付顺序
     */
    private PaymentOrder paymentOrder;

    /**
     * 交易金额
     */
    private BigDecimal price;

    /**
     * 订单取消时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTimeOut;

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

}
