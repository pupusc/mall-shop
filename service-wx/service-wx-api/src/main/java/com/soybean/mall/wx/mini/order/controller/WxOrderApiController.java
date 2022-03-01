package com.soybean.mall.wx.mini.order.controller;

import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.request.*;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/wx/mini")
@FeignClient(value = "${application.wx.name}", contextId = "WxOrderApiController")
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
    @PostMapping("/order/pay")
    BaseResponse<WxResponseBase> orderPay(@RequestBody WxOrderPayRequest request);

    /**
     * 发货
     * @param request
     * @return
     */
    @PostMapping("/order/delivery/send")
    BaseResponse<WxResponseBase> deliverySend(@RequestBody WxDeliverySendRequest request);

    @PostMapping("/order/detail")
    BaseResponse getDetail(@RequestBody WxOrderDetailRequest request);

}
