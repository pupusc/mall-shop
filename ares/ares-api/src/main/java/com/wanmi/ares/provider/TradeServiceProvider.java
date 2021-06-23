package com.wanmi.ares.provider;

import com.wanmi.ares.request.flow.FlowRequest;
import com.wanmi.ares.view.trade.TradePageView;
import com.wanmi.ares.view.trade.TradeView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;


/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:19
 */
@FeignClient(name = "${application.ares.name}", contextId="TradeServiceProvider")
public interface TradeServiceProvider {
    @PostMapping("/ares/${application.ares.version}/tarde/getTradeList")
    List<TradeView> getTradeList(@RequestBody @Valid FlowRequest request);

    @PostMapping("/ares/${application.ares.version}/tarde/getTradePage")
    TradePageView getTradePage(@RequestBody @Valid FlowRequest request);

    @PostMapping("/ares/${application.ares.version}/tarde/getOverview")
    TradeView getOverview(@RequestBody @Valid FlowRequest request);

    @PostMapping("/ares/${application.ares.version}/tarde/getStoreTradePage")
    TradePageView getStoreTradePage(@RequestBody @Valid FlowRequest request);

}
