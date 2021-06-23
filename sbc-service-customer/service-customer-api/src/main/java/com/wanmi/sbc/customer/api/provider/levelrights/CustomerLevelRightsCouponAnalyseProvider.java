package com.wanmi.sbc.customer.api.provider.levelrights;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>定时任务</p>
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerLevelRightsCouponAnalyseProvider")
public interface CustomerLevelRightsCouponAnalyseProvider {

    /**
     * 定时任务：每天夜里两点查询是否存在需要发券的权益
     *
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/task/issue-coupon")
    BaseResponse<CustomerLevelRightsListResponse> queryIssueCouponsData();
}
