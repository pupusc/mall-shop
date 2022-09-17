package com.wanmi.sbc.erp.api.provider;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.req.OrderPaymentQueryReq;
import com.wanmi.sbc.erp.api.req.OrderQueryReq;
import com.wanmi.sbc.erp.api.resp.CreateOrderResp;
import com.wanmi.sbc.erp.api.resp.OrderDetailResp;
import com.wanmi.sbc.erp.api.resp.OrderPaymentResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterOrderProvider")
public interface ShopCenterOrderProvider {


    /**
     * 电商中台创建订单
     * @param createOrderReq
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/create-order")
    BaseResponse<CreateOrderResp> createOrder(@RequestBody CreateOrderReq createOrderReq);


    /**
     * 订单详情
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/orderDetailByOrderNumber")
    BaseResponse<OrderDetailResp> orderDetailByOrderNumber(String orderNumber);


    /**
     * 订单列表
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/listOrder")
    BaseResponse<OrderDetailResp> listOrder(@RequestBody OrderQueryReq orderQueryReq);


    /**
     * 支付列表
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/payment")
    BaseResponse<OrderPaymentResp> listPayment(@RequestBody OrderPaymentQueryReq paymentQueryReq);

}
