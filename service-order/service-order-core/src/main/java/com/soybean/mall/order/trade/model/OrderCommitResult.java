package com.soybean.mall.order.trade.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import lombok.Data;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderCommitResult implements Serializable {
    private static final long serialVersionUID = -9056625560291022361L;
    /**
     * 订单编号
     */
    private String id;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    private String parentId;

    private List<TradeItem> tradeItems;

    private Consignee consignee;

    private TradePrice tradePrice;

    private Boolean couponFlag;

    /**
     * 超时未支付取消订单时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTimeOut;



}
