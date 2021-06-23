package com.wanmi.sbc.order.api.provider.payorder;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.bean.dto.StringRequest;
import com.wanmi.sbc.order.api.request.payorder.DestoryPayOrderRequest;
import com.wanmi.sbc.order.api.request.payorder.GeneratePayOrderByOrderCodeRequest;
import com.wanmi.sbc.order.api.request.payorder.UpdatePayOrderRequest;
import com.wanmi.sbc.order.api.response.payorder.DeleteByPayOrderIdResponse;
import com.wanmi.sbc.order.api.response.payorder.GeneratePayOrderByOrderCodeResponse;
import com.wanmi.sbc.order.api.response.payorder.PayOrderUpdatePayOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p> 支付单服务</p>
 * author: sunpeng
 * Date: 2018-12-5
 */
@FeignClient(value = "${application.order.name}", contextId = "PayOrderProvider")
public interface PayOrderProvider {

    /**
     * 根据订单号生成支付单
     * @param request  {@link  GeneratePayOrderByOrderCodeRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/generate-payorder")
    BaseResponse<GeneratePayOrderByOrderCodeResponse> generatePayOrderByOrderCode(@RequestBody @Valid GeneratePayOrderByOrderCodeRequest request);

    /**
     * 作废支付单
     * @param request  {@link  DestoryPayOrderRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/destory-payorder")
    BaseResponse destoryPayOrder(@RequestBody @Valid  DestoryPayOrderRequest request);

    /**
     * 修改收款单状态
     * @param request  {@link  UpdatePayOrderRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/update-payorder")
    BaseResponse<PayOrderUpdatePayOrderResponse>  updatePayOrder(@RequestBody @Valid  UpdatePayOrderRequest request);

    /**
     * 删除收款单
     * @param request  {@link  StringRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/payorder/delete-bypayorderidr")
    BaseResponse<DeleteByPayOrderIdResponse>  deleteByPayOrderId(@RequestBody @Valid StringRequest request);



}
