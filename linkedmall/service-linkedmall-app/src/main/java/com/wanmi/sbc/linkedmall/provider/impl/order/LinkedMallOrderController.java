package com.wanmi.sbc.linkedmall.provider.impl.order;


import com.aliyuncs.linkedmall.model.v20180116.*;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderProvider;
import com.wanmi.sbc.linkedmall.api.request.order.*;
import com.wanmi.sbc.linkedmall.api.response.order.SbcCreateOrderAndPayResponse;
import com.wanmi.sbc.linkedmall.order.LinkedMallOrderService;
import com.wanmi.sbc.linkedmall.util.LinkedMallUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class LinkedMallOrderController implements LinkedMallOrderProvider {
    @Autowired
    private LinkedMallOrderService linkedMallOrderService;

    /**
     * 下单并支付
     * @param sbcCreateOrderAndPayRequest
     * @return
     */
    @Override
    public BaseResponse<SbcCreateOrderAndPayResponse> createOrderAndPay(@RequestBody @Valid SbcCreateOrderAndPayRequest sbcCreateOrderAndPayRequest) {
        CreateOrderResponse createOrderResponse = linkedMallOrderService.createOrderAndPay(sbcCreateOrderAndPayRequest);
        SbcCreateOrderAndPayResponse response = new SbcCreateOrderAndPayResponse();
        if (Objects.nonNull(createOrderResponse.getModel())) {
            response.setOrderIds(createOrderResponse.getModel().getOrderIds());
            response.setPayTradeIds(createOrderResponse.getModel().getPayTradeIds());
            response.setRedirectUrl(createOrderResponse.getModel().getRedirectUrl());
            List<String> lmOrderIds = new ArrayList<>();
            createOrderResponse.getModel().getLmOrderList().forEach(lmOrderListItem -> {
                lmOrderIds.add(lmOrderListItem.getLmOrderId());
            });
            response.setLmOrderList(lmOrderIds);
        }
        return BaseResponse.success(response);
    }

    /**
     * 只下单不支付
     * @param sbcCreateOrderRequest
     * @return
     */
    @Override
    public BaseResponse<SbcCreateOrderAndPayResponse> createOrder(@RequestBody @Valid SbcCreateOrderRequest sbcCreateOrderRequest) {
        // 设置订单失效时间，秒
        sbcCreateOrderRequest.setOrderExpireTime(sbcCreateOrderRequest.getOrderExpireTime());
        CreateOrderV2Response createOrderV2Response = linkedMallOrderService.createOrder(sbcCreateOrderRequest);
        SbcCreateOrderAndPayResponse response = new SbcCreateOrderAndPayResponse();
        response.setOrderIds(createOrderV2Response.getModel().getOrderIds());
        response.setPayTradeIds(createOrderV2Response.getModel().getPayTradeIds());
        response.setRedirectUrl(createOrderV2Response.getModel().getRedirectUrl());
        List<String> lmOrderIds = new ArrayList<>();
        createOrderV2Response.getModel().getLmOrderList().forEach(lmOrderListItem -> {
            lmOrderIds.add(lmOrderListItem.getLmOrderId());
        });
        response.setLmOrderList(lmOrderIds);
        return BaseResponse.success(response);
    }

    /**
     * 订单付款，只针对 只下单不支付 订单
     * @param sbcPayOrderRequest
     * @return
     */
    @Override
    public BaseResponse<SbcCreateOrderAndPayResponse> payOrder(@Valid SbcPayOrderRequest sbcPayOrderRequest) {
        EnableOrderResponse enableOrderResponse = linkedMallOrderService.payOrder(sbcPayOrderRequest);
        SbcCreateOrderAndPayResponse response = new SbcCreateOrderAndPayResponse();
        response.setOrderIds(enableOrderResponse.getModel().getOrderIds());
        response.setPayTradeIds(enableOrderResponse.getModel().getPayTradeIds());
        response.setRedirectUrl(enableOrderResponse.getModel().getRedirectUrl());
        List<String> lmOrderIds = new ArrayList<>();
        enableOrderResponse.getModel().getLmOrderList().forEach(lmOrderListItem -> {
            lmOrderIds.add(lmOrderListItem.getLmOrderId());
        });
        response.setLmOrderList(lmOrderIds);
        return BaseResponse.success(response);
    }

    /**
     * 取消订单
     *
     * @param sbcCancelOrderRequest
     * @return
     */
    @Override
    public BaseResponse cancelOrder(@RequestBody @Valid SbcCancelOrderRequest sbcCancelOrderRequest) {
        CancelOrderResponse response = linkedMallOrderService.cancelOrder(sbcCancelOrderRequest);
        if (Objects.nonNull(response) && StringUtils.equals(LinkedMallUtil.SUCCESS_CODE_2, response.getCode())) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.error(response.getMessage());
    }

    /**
     * linkedMall 订单确认收货
     *
     * @param sbcConfirmDisburseRequest
     * @return
     */
    @Override
    public BaseResponse confirmDisburse(@RequestBody @Valid SbcConfirmDisburseRequest sbcConfirmDisburseRequest) {
        linkedMallOrderService.confirmDisburse(sbcConfirmDisburseRequest);
        return BaseResponse.SUCCESSFUL();
    }
}
