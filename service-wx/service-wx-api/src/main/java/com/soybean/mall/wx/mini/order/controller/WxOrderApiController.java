package com.soybean.mall.wx.mini.order.controller;

import com.soybean.mall.wx.mini.order.bean.request.CreateOrderRequest;
import com.soybean.mall.wx.mini.order.bean.response.CreateOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/wx/mini")
@FeignClient(value = "${application.wx.name}", contextId = "WxMiniApiController")
public interface WxOrderApiController {
    /**
     * 创建订单并返回ticket
     * @param createOrderRequest
     * @return
     */
    @PostMapping("/order/add")
    BaseResponse<CreateOrderResponse> addOrder(@RequestBody CreateOrderRequest createOrderRequest);

}
