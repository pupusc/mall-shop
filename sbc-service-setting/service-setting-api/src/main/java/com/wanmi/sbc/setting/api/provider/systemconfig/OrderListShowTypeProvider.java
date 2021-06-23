package com.wanmi.sbc.setting.api.provider.systemconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.systemconfig.OrderListShowTypeModifyRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.OrderListShowTypeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "OrderListShowTypeProvider")
public interface OrderListShowTypeProvider {

    /**
     * 更新订单列表展示状态
     *
     * @param request {@link OrderListShowTypeModifyRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/order-list-show-type/modify")
    BaseResponse modify(@RequestBody OrderListShowTypeModifyRequest request);

    /**
     * 查询订单列表展示状态
     *
     * @return 订单列表展示状态
     */
    @PostMapping("/setting/${application.setting.version}/order-list-show-type/query")
    BaseResponse<OrderListShowTypeResponse> query();
}
