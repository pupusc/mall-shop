package com.wanmi.sbc.client;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.response.DeliveryStatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "biz-supplier")
public interface BizSupplierClient {

    @RequestMapping(value = "/order/cancel",method = RequestMethod.POST)
    BaseResponse<CancelOrderResponse> cancelOrder(@RequestBody CancelOrderRequest request);

    @RequestMapping(value = "/order/delivery/status",method = RequestMethod.GET)
    BaseResponse<DeliveryStatusResponse> getDeliveryStatus(@RequestParam("pid")String pid);
}
