package com.wanmi.sbc.order.provider.impl.payorder;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderProvider;
import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
import com.wanmi.sbc.order.bean.dto.StringRequest;
import com.wanmi.sbc.order.api.request.payorder.DestoryPayOrderRequest;
import com.wanmi.sbc.order.api.request.payorder.GeneratePayOrderByOrderCodeRequest;
import com.wanmi.sbc.order.api.request.payorder.UpdatePayOrderRequest;
import com.wanmi.sbc.order.api.response.payorder.DeleteByPayOrderIdResponse;
import com.wanmi.sbc.order.api.response.payorder.GeneratePayOrderByOrderCodeResponse;
import com.wanmi.sbc.order.api.response.payorder.PayOrderUpdatePayOrderResponse;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.payorder.request.PayOrderGenerateRequest;
import com.wanmi.sbc.order.payorder.service.PayOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
public class PayOrderController implements PayOrderProvider {


    @Autowired
    private PayOrderService payOrderService;


    @Override
    public BaseResponse<GeneratePayOrderByOrderCodeResponse> generatePayOrderByOrderCode(@RequestBody @Valid GeneratePayOrderByOrderCodeRequest request) {

        PayOrderGenerateRequest payOrderGenerateRequest = PayOrderGenerateRequest.builder().payType(request.getPayType())
                .payOrderPrice(request.getPayOrderPrice())
                .orderTime(request.getOrderTime())
                .customerId(request.getCustomerId())
                .orderCode(request.getOrderCode())
                .companyInfoId(request.getCompanyInfoId())
                .build();


        Optional<PayOrder> rawPayOrder = payOrderService.generatePayOrderByOrderCode(payOrderGenerateRequest);

        if(rawPayOrder.isPresent()){

            PayOrderVO payOrder = new PayOrderVO();

            BeanUtils.copyProperties(rawPayOrder.get(), payOrder);

            return    BaseResponse.success(GeneratePayOrderByOrderCodeResponse.builder().payOrder(payOrder).build());

        }

        return  BaseResponse.FAILED();
    }

    private  List<PayOrder> toRawList(List<PayOrderDTO> voList){

        List<PayOrder> result = new ArrayList<>();

        voList.forEach( e ->{ PayOrder target = new PayOrder();BeanUtils.copyProperties(e,target);result.add(target) ;});

        return  result;
    }


    @Override
    public BaseResponse destoryPayOrder(@RequestBody @Valid DestoryPayOrderRequest request) {
        payOrderService.destoryPayOrder(toRawList(request.getPayOrders()), request.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<PayOrderUpdatePayOrderResponse> updatePayOrder(@RequestBody @Valid UpdatePayOrderRequest request) {

        payOrderService.updatePayOrder(request.getPayOrderId(),request.getPayOrderStatus());

        return BaseResponse.success(PayOrderUpdatePayOrderResponse.builder().value(request.getPayOrderId()).build());
    }

    @Override
    public BaseResponse<DeleteByPayOrderIdResponse>  deleteByPayOrderId(@RequestBody @Valid StringRequest id) {

        payOrderService.deleteByPayOrderId(id.getValue());

        return BaseResponse.success(DeleteByPayOrderIdResponse.builder().value(id.getValue()).build());

    }
}
