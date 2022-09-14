package com.wanmi.sbc.erp.api.provider;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.req.OrderQueryReq;
import com.wanmi.sbc.erp.api.resp.CreateOrderResp;
import com.wanmi.sbc.erp.api.resp.OrdOrderResp;
import com.wanmi.sbc.erp.api.resp.OrderDetailResp;
import com.wanmi.sbc.erp.api.resp.PaymentResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/detaiByOrderNumber")
    BaseResponse<OrderDetailResp> detailByOrderNumber(Long orderNumber);


    /**
     * 订单列表
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/listOrder")
    BaseResponse<List<OrderDetailResp>> listOrder(@RequestBody OrderQueryReq orderQueryReq);

    /**
     * 根据TID查询订单
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/queryMasterOrderByTid")
    BaseResponse<OrdOrderResp> queryMasterOrderByTid(Long tid);

    /**
     * 查询支付
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/getPaymentByOrderId")
    BaseResponse<List<PaymentResp>> getPaymentByOrderId(Long orderId);

}
