package com.wanmi.sbc.linkedmall.provider.impl.order;


import com.aliyuncs.linkedmall.model.v20180116.QueryOrderListResponse;
import com.aliyuncs.linkedmall.model.v20180116.RenderOrderResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.order.SbcLogisticsQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.order.SbcOrderListQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.order.SbcOrderQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.order.SbcRenderOrderRequest;
import com.wanmi.sbc.linkedmall.api.response.order.SbcLogisticsQueryResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcOrderListQueryResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcOrderQueryResponse;
import com.wanmi.sbc.linkedmall.api.response.order.SbcRenderOrderResponse;
import com.wanmi.sbc.linkedmall.order.LinkedMallOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
public class LinkedMallOrderQueryController implements LinkedMallOrderQueryProvider {
    @Autowired
    private LinkedMallOrderService linkedMallOrderService;

    /**
     * 渲染订单
     *
     * @param sbcRenderOrderRequest
     * @return
     */
    @Override
    public BaseResponse<SbcRenderOrderResponse> initRenderOrder(@RequestBody @Valid SbcRenderOrderRequest sbcRenderOrderRequest) {
        RenderOrderResponse response = linkedMallOrderService.initRenderOrder(sbcRenderOrderRequest);
        return BaseResponse.success(new SbcRenderOrderResponse(response.getModel()));
    }

    /**
     * linkedMall 订单物流查询
     *
     * @param sbcLogisticsQueryRequest
     * @return
     */
    @Override
    public BaseResponse<SbcLogisticsQueryResponse> getOrderLogistics(@RequestBody @Valid SbcLogisticsQueryRequest sbcLogisticsQueryRequest) {
        return BaseResponse.success(new SbcLogisticsQueryResponse(linkedMallOrderService.queryLogistics(sbcLogisticsQueryRequest)));
    }

    /**
     * linkedMall 根据筛选条件查询订单详情
     * @param sbcOrderListQueryRequest
     * @return
     */
    @Override
    public BaseResponse<SbcOrderListQueryResponse> queryOrderDetail(@RequestBody @Valid SbcOrderListQueryRequest sbcOrderListQueryRequest) {
        QueryOrderListResponse response = linkedMallOrderService.queryOrder(sbcOrderListQueryRequest);
        if(Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getLmOrderList())) {
            return BaseResponse.success(new SbcOrderListQueryResponse(response.getLmOrderList(), response.getPageNumber(), response.getPageSize()));
        }
        return BaseResponse.success(new SbcOrderListQueryResponse());
    }

    /**
     * linkedMall 根据主订单id查询所有子订单详情
     * @param sbcOrderQueryRequest
     * @return
     */
    @Override
    public BaseResponse<SbcOrderQueryResponse> queryOrderDetailByLmOrderId(@RequestBody @Valid SbcOrderQueryRequest sbcOrderQueryRequest) {
        List<String> lmOrderList = Collections.singletonList(sbcOrderQueryRequest.getLmOrderId());
        SbcOrderListQueryRequest sbcOrderListQueryRequest =
                SbcOrderListQueryRequest.builder().lmOrderList(lmOrderList).bizUid(sbcOrderQueryRequest.getBizUid()).build();
        QueryOrderListResponse response = linkedMallOrderService.queryOrder(sbcOrderListQueryRequest);
        if(Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getLmOrderList())) {
            return BaseResponse.success(new SbcOrderQueryResponse(response.getLmOrderList().get(0)));
        }
        return BaseResponse.success(new SbcOrderQueryResponse());
    }
}
