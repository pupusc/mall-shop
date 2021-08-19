package com.wanmi.sbc.order.trade.reponse;

import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>用于编辑订单前的订单信息展示结构</p>
 * Created by of628-wenzhi on 2018-05-25-下午4:17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRemedyDetails implements Serializable {
    private static final long serialVersionUID = 8906841191062186901L;

    /**
     * 订单详情
     */
    private Trade trade;

    /**
     * 包含skuId和计算会员，区间价后的最新单价
     */
    private Map<String, TradeItemPrice> tradeItemPriceMap;
}
