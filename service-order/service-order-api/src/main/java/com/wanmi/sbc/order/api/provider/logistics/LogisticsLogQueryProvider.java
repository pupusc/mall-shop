package com.wanmi.sbc.order.api.provider.logistics;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.logistics.LogisticsLogSimpleListByCustomerIdRequest;
import com.wanmi.sbc.order.api.response.logistics.LogisticsLogSimpleListByCustomerIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p> 服务信息查询服务</p>
 * author: dyt
 * Date: 2020-04-17
 */
@FeignClient(value = "${application.order.name}", contextId = "LogisticsLogQueryProvider")
public interface LogisticsLogQueryProvider {

    /**
     * 根据会员查询服务列表-简化
     *
     * @param request {@link  LogisticsLogSimpleListByCustomerIdRequest}
     * @return 服务列表 {@link LogisticsLogSimpleListByCustomerIdResponse}
     */
    @PostMapping("/order/${application.order.version}/logistics-log/list-by-customer-id")
    BaseResponse<LogisticsLogSimpleListByCustomerIdResponse> listByCustomerId(@RequestBody
                                                                                      LogisticsLogSimpleListByCustomerIdRequest
                                                                                      request);
}
