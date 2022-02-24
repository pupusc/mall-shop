package com.soybean.mall.order.model;

import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

@Data
public class OrderCommitResult implements Serializable {
    private static final long serialVersionUID = -9056625560291022361L;
    /**
     * 订单编号
     */
    private String tid;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    private String parentTid;

    private List<TradeItem> tradeItems;

    private Consignee consignee;

    private TradePrice tradePrice;

}
