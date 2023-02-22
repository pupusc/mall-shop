package com.wanmi.sbc.setting.tradeOrder.service;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.tradeOrder.GoodsMonthRequest;
import com.wanmi.sbc.setting.api.request.tradeOrder.OrderTradeListRequest;
import com.wanmi.sbc.setting.tradeOrder.model.root.GoodsMonth;
import com.wanmi.sbc.setting.tradeOrder.repository.GoodsMonthRepository;
import com.wanmi.sbc.setting.tradeOrder.repository.TradeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeOrderService {

    @Autowired
    TradeOrderRepository tradeOrderRepository;

    @Autowired
    GoodsMonthRepository goodsMonthRepository;

    public void addAll(OrderTradeListRequest request) {
        List<GoodsMonthRequest> list = request.getList();
        List<GoodsMonth> goodsMonth= KsBeanUtil.convert(list, GoodsMonth.class);
        goodsMonthRepository.saveAll(goodsMonth);
    }
}
