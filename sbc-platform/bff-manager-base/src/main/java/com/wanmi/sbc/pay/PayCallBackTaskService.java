package com.wanmi.sbc.pay;

import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.bean.enums.PayCallBackType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName PayCallBackService
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/7/9 19:46
 **/
@Service
public class PayCallBackTaskService {

    @Autowired
    private TradeProvider tradeProvider;

    @Async("payCallBack")
    public void payCallBack(TradePayOnlineCallBackRequest request){
        tradeProvider.payOnlineCallBack(request);
    }

    @Async
    public void pushOrderToERP(String tradeNo){
        TradePushRequest request =
                TradePushRequest.builder().tid(tradeNo).build();
        tradeProvider.pushOrderToERP(request);
    }
}
