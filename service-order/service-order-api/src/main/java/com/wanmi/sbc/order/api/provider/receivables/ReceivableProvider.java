package com.wanmi.sbc.order.api.provider.receivables;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.receivables.ReceivablesDeleteRequests;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p> 收款单服务</p>
 * author: sunpeng
 * Date: 2018-12-5
 */
@FeignClient(value = "${application.order.name}", contextId = "ReceivableProvider")
public interface ReceivableProvider {

    /**
     * 删除收款单
     * @param request  {@link ReceivablesDeleteRequests}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/receivables/delete-receivables")
    BaseResponse deleteReceivables(@RequestBody @Valid ReceivablesDeleteRequests request);
}
