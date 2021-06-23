package com.wanmi.sbc.order.provider.impl.refund;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderProvider;
import com.wanmi.sbc.order.api.request.refund.RefundOrderDeleteByIdRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderDestoryRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderRefundRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderRefuseRequest;
import com.wanmi.sbc.order.refund.model.root.RefundOrder;
import com.wanmi.sbc.order.refund.service.RefundOrderService;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.repository.ReturnOrderRepository;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 13:46
 * @version: 1.0
 */
@Validated
@RestController
public class RefundOrderController implements RefundOrderProvider{

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private ReturnOrderRepository returnOrderRepository;

    /**
     * 作废退款单
     *
     * @param refundOrderDeleteByIdRequest {@link RefundOrderDeleteByIdRequest }
     * @return {@link BaseResponse }
     */
    @Override
    public BaseResponse deleteById(@RequestBody @Valid RefundOrderDeleteByIdRequest refundOrderDeleteByIdRequest){
        refundOrderService.destory(refundOrderDeleteByIdRequest.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse destory(@RequestBody @Valid RefundOrderDestoryRequest refundOrderDestoryRequest) {
        String refundId = refundOrderDestoryRequest.getRefundId();
        Optional<RefundOrder> optional = refundOrderService.findById(refundId);
        if(optional.isPresent()){
            ReturnOrder returnOrder = returnOrderService.findById(optional.get().getReturnOrderCode());
            if(Objects.nonNull(returnOrder.getHasBeanSettled()) && returnOrder.getHasBeanSettled()){
                throw new SbcRuntimeException("K-050006");
            }
        }
        refundOrderService.destory(refundId);
        refundOrderService.findById(refundId).ifPresent(refundOrder -> {
            returnOrderService.reverse(refundOrder.getReturnOrderCode(), refundOrderDestoryRequest.getOperator());
            ReturnOrder order = returnOrderService.findById(refundOrder.getReturnOrderCode());
            if(Objects.nonNull(order) && Objects.nonNull(order.getReturnPrice().getActualReturnPrice())){
                order.getReturnPrice().setActualReturnPrice(null);
                if(order.getReturnPrice().getApplyStatus()){
                    order.getReturnPrice().setApplyStatus(false);
                    order.getReturnPrice().setApplyPrice(order.getReturnPrice().getTotalPrice());
                }
                returnOrderRepository.save(order);
            }
        });
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse refuse(@RequestBody @Valid RefundOrderRefuseRequest refundOrderRefuseRequest) {
        refundOrderService.refuse(refundOrderRefuseRequest.getId(),refundOrderRefuseRequest.getRefuseReason());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse refundFailed(@RequestBody @Valid RefundOrderRefundRequest request) {
        returnOrderService.refundFailed(request);
        return BaseResponse.SUCCESSFUL();
    }

}