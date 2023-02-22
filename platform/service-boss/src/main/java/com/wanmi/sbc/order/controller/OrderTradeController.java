package com.wanmi.sbc.order.controller;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.service.MongdbTradeService;
import com.wanmi.sbc.setting.api.provider.orderTrade.OrderTradeProvider;
import com.wanmi.sbc.setting.api.request.tradeOrder.GoodsMonthRequest;
import com.wanmi.sbc.setting.api.request.tradeOrder.OrderTradeListRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order-trade")
@Api(description = "S2B的BOSS商品服务", tags = "BossGoodsController")
public class OrderTradeController {

    @Autowired
    MongdbTradeService mongdbTradeService;

    @Autowired
    OrderTradeProvider orderTradeProvider;

    @RequestMapping(value = "/list")
    public void getGoodsOrderList(){
        List<GoodsMonthRequest> list = mongdbTradeService.getOrdeerGoodsList();
        OrderTradeListRequest orderTradeListRequest=new OrderTradeListRequest();
        orderTradeListRequest.setList(list);
        orderTradeProvider.add(orderTradeListRequest);
    }
}
