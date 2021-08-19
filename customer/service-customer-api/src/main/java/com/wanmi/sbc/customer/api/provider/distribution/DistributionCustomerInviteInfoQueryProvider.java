package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerInviteInfoPageRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerInviteInfoPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>分销员查询服务Provider</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionCustomerInviteInfoQueryProvider")
public interface DistributionCustomerInviteInfoQueryProvider {

    /**
     * 分页查询分销员邀请信息API
     * @param distributionCustomerInviteInfoPageReq 分页请求参数和筛选对象 {@link DistributionCustomerInviteInfoPageRequest}
     * @return 分销员分页列表信息 {@link DistributionCustomerInviteInfoPageResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer-invite-info/page")
    BaseResponse<DistributionCustomerInviteInfoPageResponse> page(@RequestBody @Valid DistributionCustomerInviteInfoPageRequest distributionCustomerInviteInfoPageReq);


}

