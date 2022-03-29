package com.wanmi.sbc.callback.handler;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WxPayCallBackHandler implements CallbackHandler {



    @Override
    public boolean support(String eventType) {
        return "open_product_order_pay".equals(eventType);
    }

    @Override
    public void handle(Map<String, Object> paramMap) {
        //支付回调
        log.info("WxPayCallBackHandler params:{}",paramMap);


    }
}
