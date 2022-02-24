package com.soybean.mall.wx.mini.order.controller;

import com.soybean.mall.wx.mini.order.bean.request.CreateOrderRequest;
import com.soybean.mall.wx.mini.order.bean.response.CreateOrderResponse;
import com.soybean.mall.wx.mini.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WxOrderApiControllerImpl implements WxOrderApiController {

    @Autowired
    private WxService wxService;

    @Override
    BaseResponse<CreateOrderResponse> addOrder(CreateOrderRequest createOrderRequest) {
        return BaseResponse.success(wxService.createOrder(createOrderRequest));
    }
}
