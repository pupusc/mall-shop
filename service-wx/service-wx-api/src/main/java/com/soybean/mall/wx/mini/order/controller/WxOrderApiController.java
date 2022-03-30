package com.soybean.mall.wx.mini.order.controller;

import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.request.*;
import com.soybean.mall.wx.mini.order.bean.response.GetPaymentParamsResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/order/receive")
    BaseResponse<WxResponseBase> receive(@RequestBody WxDeliveryReceiveRequest request);

    @PostMapping("/order/detail")
    BaseResponse getDetail(@RequestBody WxOrderDetailRequest request);


    /**
     * 创建售后单
     * @param request
     * @return
     */
    @PostMapping("/aftersale/add")
    BaseResponse<WxResponseBase> createAfterSale(@RequestBody WxCreateAfterSaleRequest request);

    @PostMapping("/order/getpaymentparams")
    BaseResponse<GetPaymentParamsResponse> getPaymentParams(@RequestBody WxOrderDetailRequest request);

    /****** 新售后 ******/
    /**
     * 创建售后单
     * @param request
     * @return
     */
    @PostMapping("/aftersale/create")
    BaseResponse<WxResponseBase> createNewAfterSale(@RequestBody WxCreateNewAfterSaleRequest request);

    /**
     * 同意退款
     * @param request
     * @return
     */
    @PostMapping("/aftersale/acceptRefund")
    BaseResponse<WxResponseBase> acceptRefundAfterSale(@RequestParam("returnOrder") String returnOrder);


}
