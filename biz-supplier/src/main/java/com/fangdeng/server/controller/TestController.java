package com.fangdeng.server.controller;

import com.fangdeng.server.dto.OrderTradeDTO;
import com.fangdeng.server.job.SyncGoodsJobHandler;
import com.fangdeng.server.job.SyncGoodsPriceJobHandler;
import com.fangdeng.server.job.SyncGoodsStockJobHandler;
import com.fangdeng.server.mq.ProviderTradeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private SyncGoodsJobHandler job;

    @Autowired
    private SyncGoodsPriceJobHandler priceJob;

    @Autowired
    private ProviderTradeHandler providerTradeHandler;

    @Autowired
    private SyncGoodsStockJobHandler stockJobHandler;

    @PostMapping("test")
    public void test(@RequestBody OrderTradeDTO orderTradeDTO){
        try {
            providerTradeHandler.orderPushConsumer(null,null);
        }catch (Exception e){

        }
    }
    @GetMapping("/price")
    public void price(String param){
        try {
            priceJob.execute(param);
        }catch (Exception e){

        }
    }

    @GetMapping("/goods")
    public void goods(String param){
        try {
            job.execute(param);
        }catch (Exception e){

        }
    }

    @GetMapping("/stock")
    public void stock(String param){
        try {
            stockJobHandler.execute(param);
        }catch (Exception e){

        }
    }
}
