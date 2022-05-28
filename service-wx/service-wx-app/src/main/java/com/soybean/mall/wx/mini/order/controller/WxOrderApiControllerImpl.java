package com.soybean.mall.wx.mini.order.controller;

import com.soybean.mall.wx.mini.goods.bean.response.WxListAfterSaleResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.request.*;
import com.soybean.mall.wx.mini.order.bean.response.GetPaymentParamsResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateNewAfterSaleResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxVideoOrderDetailResponse;
import com.soybean.mall.wx.mini.service.WxService;
import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public BaseResponse<WxVideoOrderDetailResponse> getDetail(WxOrderDetailRequest request) {
        return BaseResponse.success(wxService.getDetail(request));
    }

    @Override
    public BaseResponse<WxResponseBase> createAfterSale(WxCreateAfterSaleRequest request) {
        return BaseResponse.success(wxService.createAfterSale(request));
    }

    @Override
    public BaseResponse<GetPaymentParamsResponse> getPaymentParams(WxOrderDetailRequest request) {
        return BaseResponse.success(wxService.getPaymentParams(request));
    }

    @Override
    public BaseResponse<WxCreateNewAfterSaleResponse> createNewAfterSale(@RequestBody WxCreateNewAfterSaleRequest request){
        return BaseResponse.success(wxService.createNewAfterSale(request));
    }

    @Override
    public BaseResponse<WxResponseBase> acceptRefundAfterSale(WxDealAftersaleRequest wxDealAftersaleRequest){
        return BaseResponse.success(wxService.acceptRefundAfterSale(wxDealAftersaleRequest));
    }

    @Override
    public BaseResponse<WxResponseBase> cancelAfterSale(WxDealAftersaleNeedOpenidRequest wxDealAftersaleNeedOpenidRequest){
        return BaseResponse.success(wxService.cancelAfterSale(wxDealAftersaleNeedOpenidRequest));
    }

    @Override
    public BaseResponse<WxDetailAfterSaleResponse> detailAfterSale(WxDealAftersaleRequest wxDealAftersaleRequest){
        return BaseResponse.success(wxService.detailAfterSale(wxDealAftersaleRequest));
    }

    @Override
    public BaseResponse<WxResponseBase> acceptReturnAfterSale(WxAcceptReturnAftersaleRequest wxAcceptReturnAftersaleRequest){
        return BaseResponse.success(wxService.acceptReturnAfterSale(wxAcceptReturnAftersaleRequest));
    }

    @Override
    public BaseResponse<WxResponseBase> rejectAfterSale(WxDealAftersaleRequest wxDealAftersaleRequest){
        return BaseResponse.success(wxService.rejectAfterSale(wxDealAftersaleRequest));
    }

    @Override
    public BaseResponse<WxResponseBase> uploadReturnInfo(WxUploadReturnInfoRequest request) {
        return BaseResponse.success(wxService.uploadReturnInfo(request));
    }

    @Override
    public BaseResponse<WxListAfterSaleResponse> listAfterSale(WxAfterSaleListRequest request) {
        return BaseResponse.success(wxService.listAfterSale(request));
    }

    @Override
    public BaseResponse<WxResponseBase> cancelOrder(WxOrderCancelRequest request) {
        return BaseResponse.success(wxService.cancelOrder(request));
    }

    @Override
    public BaseResponse<WxResponseBase> updateAfterSaleOrder(WxAfterSaleUpdateRequest request) {
        return BaseResponse.success(wxService.updateAfterSaleOrder(request));
    }
}
