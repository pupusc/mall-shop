package com.wanmi.sbc.order.provider.impl.refund;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.request.refund.*;
import com.wanmi.sbc.order.api.request.trade.TradeAccountRecordRequest;
import com.wanmi.sbc.order.api.response.refund.*;
import com.wanmi.sbc.order.api.response.trade.TradeAccountRecordResponse;
import com.wanmi.sbc.order.refund.model.root.RefundOrder;
import com.wanmi.sbc.order.refund.service.RefundOrderService;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 13:46
 * @version: 1.0
 */
@Validated
@RestController
public class RefundOrderQueryController implements RefundOrderQueryProvider{

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private ReturnOrderService returnOrderService;

    /**
     * 查询退款单
     * @param refundOrderPageRequest {@link RefundOrderPageRequest }
     * @return {@link RefundOrderPageResponse }
    */
    @Override
    public BaseResponse<RefundOrderPageResponse> page(@RequestBody @Valid RefundOrderPageRequest refundOrderPageRequest){
        RefundOrderPageResponse refundOrderPageResponse = refundOrderService.findByRefundOrderRequest(KsBeanUtil.convert(refundOrderPageRequest,RefundOrderRequest.class));
        return BaseResponse.success(refundOrderPageResponse);
    }

    /**
     * 查询不带分页的退款单
     * @param refundOrderWithoutPageRequest {@link RefundOrderWithoutPageRequest }
     * @return {@link RefundOrderWithoutPageResponse }
    */
    @Override
    public BaseResponse<RefundOrderWithoutPageResponse> list(@RequestBody @Valid RefundOrderWithoutPageRequest refundOrderWithoutPageRequest){
        RefundOrderPageResponse refundOrderPageResponse = refundOrderService.findByRefundOrderRequestWithNoPage(KsBeanUtil.convert(refundOrderWithoutPageRequest,RefundOrderRequest.class));
        return BaseResponse.success(KsBeanUtil.convert(refundOrderPageResponse,RefundOrderWithoutPageResponse.class));
    }

    /**
     * 根据退单编号查询退款单
     * @param refundOrderByReturnOrderCodeRequest 包含：退单编号 {@link RefundOrderByReturnOrderCodeRequest }
     * @return  {@link RefundOrderByReturnCodeResponse }
    */
    @Override
    public BaseResponse<RefundOrderByReturnCodeResponse> getByReturnOrderCode(@RequestBody @Valid RefundOrderByReturnOrderCodeRequest refundOrderByReturnOrderCodeRequest){
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(refundOrderByReturnOrderCodeRequest.getReturnOrderCode());
        return BaseResponse.success(KsBeanUtil.convert(refundOrder,RefundOrderByReturnCodeResponse.class));
    }

    /**
     * 根据退单ID查询退款单
     * @param refundOrderByIdRequest 包含：退单ID {@link RefundOrderByIdRequest }
     * @return  {@link RefundOrderByIdResponse }
     */
    @Override
    public BaseResponse<RefundOrderByIdResponse> getById(@RequestBody @Valid RefundOrderByIdRequest refundOrderByIdRequest){
        RefundOrder refundOrder = refundOrderService.findById(refundOrderByIdRequest.getRefundId()).orElseGet(() -> new RefundOrder());
        return BaseResponse.success(KsBeanUtil.convert(refundOrder,RefundOrderByIdResponse.class));
    }

    /**
     * 根据退单编号查询退款单
     * @param refundOrderResponseByReturnOrderCodeRequest {@link RefundOrderResponseByReturnOrderCodeRequest }
     * @return {@link RefundOrderResponse }
    */
    @Override
    public BaseResponse<RefundOrderListReponse> getRefundOrderRespByReturnOrderCode(@RequestBody @Valid RefundOrderResponseByReturnOrderCodeRequest refundOrderResponseByReturnOrderCodeRequest){
        List<RefundOrderResponse> list = new ArrayList<>();
        ReturnOrder returnOrder = returnOrderService.findById(refundOrderResponseByReturnOrderCodeRequest.getReturnOrderCode());
        com.wanmi.sbc.order.bean.vo.RefundOrderResponse refundOrderResponse = refundOrderService.findRefundOrderRespByReturnOrderNo(refundOrderResponseByReturnOrderCodeRequest.getReturnOrderCode());
        list.add(KsBeanUtil.convert(refundOrderResponse, RefundOrderResponse.class));
        if(StringUtils.isNotBlank(returnOrder.getBusinessTailId())) {
            RefundOrderResponse convert = KsBeanUtil.convert(refundOrderService.findRefundOrderRespByReturnOrderNo(returnOrder.getBusinessTailId()), RefundOrderResponse.class);
            list.add(convert);
        }
       return BaseResponse.success(new RefundOrderListReponse(list));
    }

    /**
     * 合计退款金额
     * @param refundOrderRequest {@link RefundOrderRequest }
     * @return {@link RefundOrderGetSumReturnPriceResponse }
    */
    @Override
    public BaseResponse<RefundOrderGetSumReturnPriceResponse> getSumReturnPrice(@RequestBody @Valid RefundOrderRequest refundOrderRequest){
        BigDecimal result = refundOrderService.sumReturnPrice(refundOrderRequest);
        return BaseResponse.success(new RefundOrderGetSumReturnPriceResponse(result));
    }

    @Override
    public BaseResponse<RefundOrderByReturnOrderNoResponse> getByReturnOrderNo(@RequestBody @Valid RefundOrderByReturnOrderNoRequest refundOrderByReturnOrderCodeRequest){
        RefundOrder refundOrder = refundOrderService.getRefundOrderByReturnOrderNo(refundOrderByReturnOrderCodeRequest.getReturnOrderCode());
        return BaseResponse.success(KsBeanUtil.convert(refundOrder,RefundOrderByReturnOrderNoResponse.class));
    }


    /**
     * 查询对账信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<RefundOrderAccountRecordResponse> getRefundOrderAccountRecord(@Valid @RequestBody RefundOrderAccountRecordRequest request) {
        return BaseResponse.success(refundOrderService.getRefundOrderAccountRecord(request));
    }


}
