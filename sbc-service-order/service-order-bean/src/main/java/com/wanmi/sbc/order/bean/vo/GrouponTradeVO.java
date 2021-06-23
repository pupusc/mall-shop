package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.vo.TradeGrouponVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单状态
 * Created by jinwei on 14/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class GrouponTradeVO implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String id;

    /**
     * 订单组号
     */
    @ApiModelProperty(value = "订单组号")
    private String groupId;

    /**
     * 商家
     */
    @ApiModelProperty(value = "商家")
    private SupplierVO supplier;

    /**
     * boss卖方
     */
    @ApiModelProperty(value = "boss卖方")
    private SellerVO seller;

    /**
     * 订单实际支付金额
     * 账务中心每次回调的支付金额之和：订单已支付金额
     * add wumeng
     */
    @ApiModelProperty(value = "订单实际支付金额")
    private BigDecimal totalPayCash;

    /**
     * 超时未支付取消订单时间
     */
    @ApiModelProperty(value = "超时未支付取消订单时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTimeOut;

    /**
     * 订单价格
     */
    @ApiModelProperty(value = "订单价格")
    private TradePriceVO tradePrice;

    /**
     * 团实例信息
     */
    private GrouponInstanceVO grouponInstance;

    /**
     * 订单商品列表
     */
    @ApiModelProperty(value = "订单商品列表")
    private List<TradeItemVO> tradeItems = new ArrayList<>();

    /**
     * 订单拼团信息
     */
    private TradeGrouponVO tradeGroupon;

    /**
     * 订单总体状态
     */
    @ApiModelProperty(value = "订单总体状态")
    private TradeStateVO tradeState;
}
