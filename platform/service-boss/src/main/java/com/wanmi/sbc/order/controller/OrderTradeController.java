package com.wanmi.sbc.order.controller;


import com.wanmi.sbc.order.service.MongdbTradeService;
import com.wanmi.sbc.setting.api.request.tradeOrder.GoodsMonthRequest;
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void getGoodsOrderList(){
        List<GoodsMonthRequest> list = mongdbTradeService.getList();
    }
}
