package com.wanmi.sbc.order.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feitingting on 2017/8/4.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeDeliverRecord {
    /**
     * 发货记录
     */
    private List<TradeDeliver> tradeDeliver = new ArrayList<>();

    /**
     * 订单总体状态
     */
    private String status;
}
