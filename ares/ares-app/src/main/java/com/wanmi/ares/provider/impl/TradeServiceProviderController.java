package com.wanmi.ares.provider.impl;

import com.wanmi.ares.provider.TradeServiceProvider;
import com.wanmi.ares.request.flow.FlowRequest;
import com.wanmi.ares.thrift.TradeServiceImpl;
import com.wanmi.ares.view.trade.TradePageView;
import com.wanmi.ares.view.trade.TradeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:23
 */
@RestController
public class TradeServiceProviderController implements TradeServiceProvider {

    @Autowired
    private TradeServiceImpl tradeService;

    @Override
    public List<TradeView> getTradeList(@RequestBody @Valid FlowRequest request)  {
        return tradeService.getTradeList(request);
    }

    @Override
    public TradePageView getTradePage(@RequestBody @Valid FlowRequest request)  {
        return tradeService.getTradePage(request);
    }

    @Override
    public TradeView getOverview(@RequestBody @Valid FlowRequest request)  {
        return tradeService.getOverview(request);
    }

    @Override
    public TradePageView getStoreTradePage(@RequestBody @Valid FlowRequest request) {
        return tradeService.getStoreTradePage(request);
    }
}
