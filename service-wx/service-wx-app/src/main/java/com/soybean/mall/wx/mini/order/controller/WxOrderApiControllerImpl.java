package com.soybean.mall.wx.mini.order.controller;

import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.request.*;
import com.soybean.mall.wx.mini.order.bean.response.GetPaymentParamsResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.soybean.mall.wx.mini.service.WxService;
import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WxOrderApiControllerImpl implements WxOrderApiController {

    @Autowired
    private WxService wxService;


    @Override
    public BaseResponse<WxCreateOrderResponse> addOrder(WxCreateOrderRequest createOrderRequest) {
        return BaseResponse.success(wxService.createOrder(createOrderRequest));
    }

    @Override
    public BaseResponse<WxResponseBase> orderPay(WxOrderPayRequest request) {
        return BaseResponse.success(wxService.orderPay(request));
    }

    @Override
    public BaseResponse<WxResponseBase> deliverySend(WxDeliverySendRequest request) {
        return BaseResponse.success(wxService.deliverySend(request));
    }

    @Override
    public BaseResponse<WxResponseBase> receive(WxDeliveryReceiveRequest request) {
        return BaseResponse.success(wxService.deliveryReceive(request));
    }

    @Override
    public BaseResponse getDetail(WxOrderDetailRequest request) {
        wxService.getOrder(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<WxResponseBase> createAfterSale(WxCreateAfterSaleRequest request) {
        return BaseResponse.success(wxService.createAfterSale(request));
    }

    @Override
    public BaseResponse<GetPaymentParamsResponse> getPaymentParams(WxOrderDetailRequest request) {
        return BaseResponse.success(wxService.getPaymentParams(request));
    }
}
