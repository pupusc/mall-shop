package com.wanmi.sbc.order.api.provider.orderinvoice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.orderinvoice.*;
import com.wanmi.sbc.order.api.response.orderinvoice.OrderInvoiceGenerateResponse;
import com.wanmi.sbc.order.api.response.orderinvoice.OrderInvoiceModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-30 11:23
 */
@FeignClient(value = "${application.order.name}", contextId = "OrderInvoiceProvider")
public interface OrderInvoiceProvider {

    /**
     * 生成开票
     * @param generateRequest 订单开发生成参数 {@link OrderInvoiceGenerateRequest}
     * @return  订单开票信息 {@link OrderInvoiceGenerateResponse}
     */
    @PostMapping("/order/${application.order.version}/invoice/generate-order-invoice")
    BaseResponse<OrderInvoiceGenerateResponse> generateOrderInvoice(@RequestBody @Valid OrderInvoiceGenerateRequest generateRequest);

    /**
     * 更新
     * @param modifyRequest 订单开发修改参数 {@link OrderInvoiceModifyRequest}
     * @return 订单开票信息 {@link OrderInvoiceModifyResponse}
     */
    @PostMapping("/order/${application.order.version}/invoice/modify")
    BaseResponse<OrderInvoiceModifyResponse> modify(@RequestBody @Valid OrderInvoiceModifyRequest modifyRequest);

    /**
     * 更新开票状态
     * @param orderInvoiceModifyStateRequest 订单开发修改参数 {@link OrderInvoiceModifyStateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/invoice/modify-OrderInvoiceState")
    BaseResponse modifyOrderInvoiceState(@RequestBody @Valid OrderInvoiceModifyStateRequest orderInvoiceModifyStateRequest);

    /**
     * 废弃开票
     * @param invalidRequest 订单开票id {@link OrderInvoiceInvalidRequest}
     * @return  操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/invoice/invalid")
    BaseResponse invalid(@RequestBody @Valid OrderInvoiceInvalidRequest invalidRequest);

    /**
     * 删除开票
     * @param deleteRequest 订单开票id {@link OrderInvoiceDeleteRequest}
     * @return  操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/invoice/delete")
    BaseResponse delete(@RequestBody @Valid OrderInvoiceDeleteRequest deleteRequest);

    /**
     * 删除开票
     * @param deleteRequest 订单号 {@link OrderInvoiceDeleteByOrderNoRequest}
     * @return  操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/invoice/delete-by-orderNo")
    BaseResponse deleteByOrderNo(@RequestBody @Valid OrderInvoiceDeleteByOrderNoRequest deleteRequest);




}
