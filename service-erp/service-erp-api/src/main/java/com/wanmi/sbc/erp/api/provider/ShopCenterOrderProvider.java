package com.wanmi.sbc.erp.api.provider;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.req.OrdItemReq;
import com.wanmi.sbc.erp.api.resp.CreateOrderResp;
import com.wanmi.sbc.erp.api.resp.OrdItemResp;
import com.wanmi.sbc.erp.api.resp.OrdOrderResp;
import com.wanmi.sbc.erp.api.resp.OrderDetailResp;
import com.wanmi.sbc.erp.api.resp.PaymentResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterOrderProvider")
public interface ShopCenterOrderProvider {


    /**
     * 电商中台创建订单
     * @param createOrderReq
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/createOrder")
    BaseResponse<CreateOrderResp> createOrder(@RequestBody CreateOrderReq createOrderReq);

    /**
     * 订单详情（根据第三方订单号）
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/order/detailByPlatformOrderId")
    BaseResponse<OrderDetailResp> detailByPlatformOrderId(@RequestParam("platformOrderId") String platformOrderId);

//    /**
//     * 根据TID查询订单
//     */
//    @PostMapping("/erp/${application.erp.version}/shopcenter/order/queryMasterOrderByTid")
//    BaseResponse<OrdOrderResp> queryMasterOrderByTid(Long tid);


//    @PostMapping("/erp/${application.erp.version}/shopcenter/order/getPaymentByOrderId")
//    BaseResponse<List<PaymentResp>> getPaymentByOrderId(Long orderId);


    @PostMapping("/erp/${application.erp.version}/shopcenter/order/listOrdItem")
    BaseResponse<List<OrdItemResp>> listOrdItem(@RequestBody OrdItemReq request);

}
