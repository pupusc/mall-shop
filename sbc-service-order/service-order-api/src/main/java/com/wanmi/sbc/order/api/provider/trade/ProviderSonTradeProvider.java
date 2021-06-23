package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.FindProviderTradeRequest;
import com.wanmi.sbc.order.api.response.trade.FindProviderTradeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 查询商品子订单
 * @Autho caiping
 */
@FeignClient(value = "${application.order.name}", contextId = "ProviderSonTradeProvider")
public interface ProviderSonTradeProvider {

    /**
     * 查询子订单
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/provider/son-trade")
    BaseResponse<FindProviderTradeResponse> findSonTradeByParentId(@RequestBody @Valid FindProviderTradeRequest request);
}
