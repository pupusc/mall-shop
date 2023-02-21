package com.wanmi.sbc.setting.api.provider.orderTrade;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.tradeOrder.GoodsMonthRequest;
import com.wanmi.sbc.setting.api.request.tradeOrder.OrderTradeListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${application.setting.name}", contextId = "OrderTradeProvider")
public interface OrderTradeProvider {

    @PostMapping("/setting/${application.setting.version}/orderTrade/add")
    BaseResponse add(@RequestBody OrderTradeListRequest request);
}
