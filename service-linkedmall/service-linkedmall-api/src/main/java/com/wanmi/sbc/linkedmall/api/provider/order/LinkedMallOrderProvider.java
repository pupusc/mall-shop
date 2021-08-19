package com.wanmi.sbc.linkedmall.api.provider.order;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.order.*;
import com.wanmi.sbc.linkedmall.api.response.order.SbcCreateOrderAndPayResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: yhy
 * \* Date: 2020-8-10
 * \* Time: 17:33
 */
@FeignClient(value = "${application.linkedmall.name}",contextId = "LinkedMallOrderProvider")
public interface LinkedMallOrderProvider {

    /**
     * 下单并支付
     * @param sbcCreateOrderAndPayRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/create-order-and-pay")
    BaseResponse<SbcCreateOrderAndPayResponse> createOrderAndPay(@RequestBody @Valid SbcCreateOrderAndPayRequest sbcCreateOrderAndPayRequest);

    /**
     * 只下单不支付
     * @param sbcCreateOrderRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/create-order")
    BaseResponse<SbcCreateOrderAndPayResponse> createOrder(@RequestBody @Valid SbcCreateOrderRequest sbcCreateOrderRequest);

    /**
     * 订单付款，只针对 只下单不支付 订单
     * @param sbcPayOrderRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/pay-order")
    BaseResponse<SbcCreateOrderAndPayResponse> payOrder(@RequestBody @Valid SbcPayOrderRequest sbcPayOrderRequest);

    /**
     * 取消订单
     * @param sbcCancelOrderRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/cancel-order")
    BaseResponse cancelOrder(@RequestBody @Valid SbcCancelOrderRequest sbcCancelOrderRequest);

    /**
     * linkedMall 订单确认收货
     * @param sbcConfirmDisburseRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/confirm-disburse")
    BaseResponse confirmDisburse(@RequestBody @Valid SbcConfirmDisburseRequest sbcConfirmDisburseRequest);


}
