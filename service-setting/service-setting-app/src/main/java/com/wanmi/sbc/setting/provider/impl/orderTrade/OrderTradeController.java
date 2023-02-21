package com.wanmi.sbc.setting.provider.impl.orderTrade;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.orderTrade.OrderTradeProvider;
import com.wanmi.sbc.setting.api.request.tradeOrder.GoodsMonthRequest;
import com.wanmi.sbc.setting.api.request.tradeOrder.OrderTradeListRequest;
import com.wanmi.sbc.setting.tradeOrder.service.TradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderTradeController implements OrderTradeProvider {

    @Autowired
    TradeOrderService tradeOrderService;

    @Override
    public BaseResponse add(OrderTradeListRequest request) {
        tradeOrderService.addAll(request);
        return BaseResponse.SUCCESSFUL();
    }
}
