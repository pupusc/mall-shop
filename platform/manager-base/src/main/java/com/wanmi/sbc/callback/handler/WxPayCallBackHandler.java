package com.wanmi.sbc.callback.handler;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.trade.WxTradePayCallBackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WxPayCallBackHandler implements CallbackHandler {


    @Autowired
    private TradeProvider tradeProvider;

    @Override
    public boolean support(String eventType) {
        return "open_product_order_pay".equals(eventType);
    }

    @Override
    public String handle(Map<String, Object> paramMap) {
        //支付回调
        log.info("WxPayCallBackHandler params:{}",paramMap);
        try {
            Map<String, String> orderResult = (Map<String, String>) paramMap.get("order_info");
            WxTradePayCallBackRequest request = new WxTradePayCallBackRequest();
            request.setOrderId(orderResult.get("out_order_id"));
            request.setTransationId(orderResult.get("transaction_id"));
            tradeProvider.wxPayCallBack(request);
        }catch (Exception e){
            log.error("WxPayCallBackHandler error,param:{}",paramMap,e);
        }
        return "success";
    }
}
