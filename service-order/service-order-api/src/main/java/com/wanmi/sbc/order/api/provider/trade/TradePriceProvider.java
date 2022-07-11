package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.TradePriceParamBO;
import com.wanmi.sbc.order.api.response.trade.TradePriceResultBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Liang Jun
 * @date 2022-06-28 13:38:00
 */
@FeignClient(value = "${application.order.name}", contextId = "TradePriceProvider", url = "http://127.0.0.1:8970")
public interface TradePriceProvider {

    @PostMapping("/order/${application.order.version}/trade/calcPrice")
    BaseResponse<TradePriceResultBO> calcPrice(@RequestBody @Valid TradePriceParamBO paramBO);
}
