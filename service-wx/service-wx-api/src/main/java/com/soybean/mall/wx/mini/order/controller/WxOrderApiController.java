package com.soybean.mall.wx.mini.order.controller;

import com.soybean.mall.wx.mini.goods.bean.response.WxListAfterSaleResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.request.*;
import com.soybean.mall.wx.mini.order.bean.response.GetPaymentParamsResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateNewAfterSaleResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxCreateOrderResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.soybean.mall.wx.mini.order.bean.response.WxVideoOrderDetailResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    BaseResponse<WxVideoOrderDetailResponse> getDetail(@RequestBody WxOrderDetailRequest request);


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
     */
    @PostMapping("/aftersale/create")
    BaseResponse<WxCreateNewAfterSaleResponse> createNewAfterSale(@RequestBody WxCreateNewAfterSaleRequest request);

    /**
     * 同意退款
     */
    @PostMapping("/aftersale/accept-refund")
    BaseResponse<WxResponseBase> acceptRefundAfterSale(@RequestBody WxDealAftersaleRequest wxDealAftersaleRequest);

    /**
     * 取消售后
     */
    @PostMapping("/aftersale/cancel")
    BaseResponse<WxResponseBase> cancelAfterSale(@RequestBody WxDealAftersaleNeedOpenidRequest wxDealAftersaleNeedOpenidRequest);

    /**
     * 售后单详情
     */
    @PostMapping("/aftersale/detail")
    BaseResponse<WxDetailAfterSaleResponse> detailAfterSale(@RequestBody WxDealAftersaleRequest wxDealAftersaleRequest);

    /**
     * 售后-同意退货
     */
    @PostMapping("/aftersale/accept-return")
    BaseResponse<WxResponseBase> acceptReturnAfterSale(@RequestBody WxAcceptReturnAftersaleRequest wxAcceptReturnAftersaleRequest);

    /**
     * 售后-拒绝售后
     */
    @PostMapping("/aftersale/reject")
    BaseResponse<WxResponseBase> rejectAfterSale(@RequestBody WxDealAftersaleRequest wxDealAftersaleRequest);


    /**
     * 售后-用户上传物流信息
     */
    @PostMapping("/aftersale/uploadreturninfo")
    BaseResponse<WxResponseBase> uploadReturnInfo(@RequestBody WxUploadReturnInfoRequest request);

    /**
     * 售后单列表
     */
    @PostMapping("/aftersale/list")
    BaseResponse<WxListAfterSaleResponse> listAfterSale(@RequestBody WxAfterSaleListRequest wxDealAftersaleRequest);


    /**
     * 取消订单
     */
    @PostMapping("/order/cancel")
    BaseResponse<WxResponseBase> cancelOrder(@RequestBody WxOrderCancelRequest request);


    /**
     * 更新售后订单信息
     */
    BaseResponse<WxResponseBase> updateAfterSaleOrder(@RequestBody WxAfterSaleUpdateRequest request);
}
