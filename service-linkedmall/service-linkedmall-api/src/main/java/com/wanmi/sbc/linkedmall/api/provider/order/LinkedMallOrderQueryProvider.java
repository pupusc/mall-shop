package com.wanmi.sbc.linkedmall.api.provider.order;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.order.SbcLogisticsQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.order.SbcOrderListQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.order.SbcOrderQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.order.SbcRenderOrderRequest;
import com.wanmi.sbc.linkedmall.api.response.order.SbcLogisticsQueryResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcOrderListQueryResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcOrderQueryResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcRenderOrderResponse;
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
@FeignClient(value = "${application.linkedmall.name}",contextId = "LinkedMallOrderQueryProvider")
public interface LinkedMallOrderQueryProvider {


    /**
     * 渲染订单：是否可售、不可售原因、配送方式、邮费
     * @param sbcRenderOrderRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/init-render-order")
    BaseResponse<SbcRenderOrderResponse> initRenderOrder(@RequestBody @Valid SbcRenderOrderRequest sbcRenderOrderRequest);

    /**
     * linkedMall 订单物流查询
     * @param sbcLogisticsQueryRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/get-order-logistics")
    BaseResponse<SbcLogisticsQueryResponse> getOrderLogistics(@RequestBody @Valid SbcLogisticsQueryRequest sbcLogisticsQueryRequest);

    /**
     * linkedMall 根据筛选条件查询订单详情
     * @param sbcOrderListQueryRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/query-order-detail")
    BaseResponse<SbcOrderListQueryResponse> queryOrderDetail(@RequestBody @Valid SbcOrderListQueryRequest sbcOrderListQueryRequest);

    /**
     * linkedMall 根据主订单id查询子订单详情
     * @param sbcOrderQueryRequest
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/order/query-order-detail-by-lmOrderId")
    BaseResponse<SbcOrderQueryResponse> queryOrderDetailByLmOrderId(@RequestBody @Valid SbcOrderQueryRequest sbcOrderQueryRequest);


}
