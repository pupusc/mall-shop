package com.wanmi.sbc.order.provider.impl.orderinvoice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.orderinvoice.OrderInvoiceProvider;
import com.wanmi.sbc.order.api.request.orderinvoice.*;
import com.wanmi.sbc.order.api.response.orderinvoice.OrderInvoiceGenerateResponse;
import com.wanmi.sbc.order.api.response.orderinvoice.OrderInvoiceModifyResponse;
import com.wanmi.sbc.order.bean.vo.OrderInvoiceVO;
import com.wanmi.sbc.order.orderinvoice.model.root.OrderInvoice;
import com.wanmi.sbc.order.orderinvoice.request.OrderInvoiceSaveRequest;
import com.wanmi.sbc.order.orderinvoice.service.OrderInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-03 10:44
 */
@Validated
@RestController
public class OrderInvoiceController implements OrderInvoiceProvider {

    @Autowired
    private OrderInvoiceService orderInvoiceService;


    /**
     * 生成开票
     * @param request 订单开发生成参数 {@link OrderInvoiceGenerateRequest}
     * @return  订单开票信息 {@link OrderInvoiceGenerateResponse}
     */
    @Override
    public BaseResponse<OrderInvoiceGenerateResponse> generateOrderInvoice(@RequestBody @Valid OrderInvoiceGenerateRequest request) {
        Optional<OrderInvoice> orderInvoice = orderInvoiceService.generateOrderInvoice(KsBeanUtil.convert(request.getOrderInvoiceDTO(),
                OrderInvoiceSaveRequest.class), request.getEmployeeId(), request.getInvoiceState());
        return BaseResponse.success(OrderInvoiceGenerateResponse.builder()
                .orderInvoiceVO(KsBeanUtil.convert(orderInvoice.get(),OrderInvoiceVO.class ))
                .build());
    }

    /**
     * 更新
     * @param modifyRequest 订单开发修改参数 {@link OrderInvoiceModifyRequest}
     * @return 订单开票信息 {@link OrderInvoiceModifyResponse}
     */
    @Override
    public BaseResponse<OrderInvoiceModifyResponse> modify(@RequestBody @Valid OrderInvoiceModifyRequest modifyRequest) {
        Optional<OrderInvoice> orderInvoice = orderInvoiceService.updateOrderInvoice(KsBeanUtil.convert(modifyRequest,
                OrderInvoiceSaveRequest.class));
        return BaseResponse.success(OrderInvoiceModifyResponse.builder()
                .orderInvoiceVO(KsBeanUtil.convert(orderInvoice.get(),OrderInvoiceVO.class ))
                .build());
    }

    /**
     * 更新开票状态
     * @param orderInvoiceModifyStateRequest 订单开发修改参数 {@link OrderInvoiceModifyStateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyOrderInvoiceState(@RequestBody @Valid OrderInvoiceModifyStateRequest orderInvoiceModifyStateRequest) {
        orderInvoiceService.updateOrderInvoiceState(orderInvoiceModifyStateRequest.getOrderInvoiceIds());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 废弃开票
     * @param invalidRequest 订单开票id {@link OrderInvoiceInvalidRequest}
     * @return  操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse invalid(@RequestBody @Valid OrderInvoiceInvalidRequest invalidRequest) {
        orderInvoiceService.invalidInvoice(invalidRequest.getOrderInvoiceId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除开票
     * @param deleteRequest 订单开票id {@link OrderInvoiceDeleteRequest}
     * @return  操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse delete(@RequestBody @Valid OrderInvoiceDeleteRequest deleteRequest) {
        orderInvoiceService.deleteOrderInvoice(deleteRequest.getOrderInvoiceId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除开票
     * @param deleteRequest 订单号 {@link OrderInvoiceDeleteByOrderNoRequest}
     * @return  操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse deleteByOrderNo(@RequestBody @Valid OrderInvoiceDeleteByOrderNoRequest deleteRequest) {
        orderInvoiceService.deleteOrderInvoiceByOrderNo(deleteRequest.getOrderNo());
        return BaseResponse.SUCCESSFUL();
    }

}
