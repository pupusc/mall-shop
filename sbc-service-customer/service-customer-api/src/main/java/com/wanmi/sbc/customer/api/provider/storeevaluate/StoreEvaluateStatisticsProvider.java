package com.wanmi.sbc.customer.api.provider.storeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author liutao
 * @date 2019/2/27 11:09 AM
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreEvaluateStatisticsProvider")
public interface StoreEvaluateStatisticsProvider {

    /**
     * 统计商家评价 30 90 180天的数据
     *
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/evaluate/statistics")
    BaseResponse statistics();
}
