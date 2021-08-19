package com.wanmi.sbc.order.api.provider.linkedmall;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.linkedmall.LinkedMallReturnOrderReasonRequest;
import com.wanmi.sbc.order.api.response.linkedmall.LinkedMallReturnReasonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>linkedMall退单服务查询接口</p>
 * @Author: daiyitian
 * @Description: 退单服务查询接口
 * @Date: 2018-12-03 15:40
 */
@FeignClient(value = "${application.order.name}", contextId = "SbcLinkedMallReturnOrderQueryProvider")
public interface LinkedMallReturnOrderQueryProvider {

    /**
     * 查询所有退货原因
     *
     * @param request  退货原因请求参数 {@link LinkedMallReturnOrderReasonRequest}
     * @return 退货原因列表 {@link LinkedMallReturnReasonResponse}
     */
    @PostMapping("/order/${application.order.version}/linked-mall-return/list-return-reason")
    BaseResponse<LinkedMallReturnReasonResponse> listReturnReason(@RequestBody @Valid LinkedMallReturnOrderReasonRequest request);
}
