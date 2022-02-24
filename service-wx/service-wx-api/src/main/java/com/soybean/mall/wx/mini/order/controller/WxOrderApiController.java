package com.soybean.mall.wx.mini.order.controller;

import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import com.soybean.mall.wx.mini.order.bean.request.WxOrderPayRequest;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.wanmi.sbc.common.base.BaseResponse;
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
    BaseResponse<WxCreateOrderResponse> addOrder(@RequestBody WxCreateOrderRequest createOrderRequest);


    /**
     * 同步支付结果
     * @param request
     * @return
     */
    @PostMapping("/order/add")
    BaseResponse<WxResponseBase> orderPay(@RequestBody WxOrderPayRequest request);

}
