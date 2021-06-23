package com.wanmi.sbc.order.api.provider.refund;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.refund.RefundOrderDeleteByIdRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderDestoryRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderRefundRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderRefuseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 13:46
 * @version: 1.0
 */
@FeignClient(value = "${application.order.name}", contextId = "RefundOrderProvider")
public interface RefundOrderProvider {

    /**
     * 作废退款单
     *
     * @param refundOrderDeleteByIdRequest {@link RefundOrderDeleteByIdRequest }
     * @return {@link BaseResponse }
     */
    @PostMapping("/order/${application.order.version}/refund/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid RefundOrderDeleteByIdRequest refundOrderDeleteByIdRequest);

    /**
     * 销毁
     *
     * @param refundOrderDestoryRequest {@link RefundOrderDestoryRequest }
     * @return {@link BaseResponse }
     */
    @PostMapping("/order/${application.order.version}/refund/destory")
    BaseResponse destory(@RequestBody @Valid RefundOrderDestoryRequest refundOrderDestoryRequest);

    /**
     * 拒绝退款
     *
     * @param refundOrderRefuseRequest {@link RefundOrderRefuseRequest }
     * @return
     */
    @PostMapping("/order/${application.order.version}/refund/refuse")
    BaseResponse refuse(@RequestBody @Valid RefundOrderRefuseRequest refundOrderRefuseRequest);

    /**
     * 退款失败
     *
     * @param refundOrderRefundRequest
     * @return
     */
    @PostMapping("/order/${application.order.version}/refund/failed")
    BaseResponse refundFailed(@RequestBody @Valid RefundOrderRefundRequest refundOrderRefundRequest);


}