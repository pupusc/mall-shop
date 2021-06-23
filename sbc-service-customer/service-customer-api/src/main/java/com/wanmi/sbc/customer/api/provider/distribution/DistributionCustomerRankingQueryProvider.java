package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingPageRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingPageResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>用户分销排行榜查询服务Provider</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionCustomerRankingQueryProvider")
public interface DistributionCustomerRankingQueryProvider {

    /**
     * 分页查询用户分销排行榜API
     * @param distributionCustomerRankingPageReq 分页请求参数和筛选对象 {@link DistributionCustomerRankingPageRequest}
     * @return 用户分销排行榜分页列表信息 {@link DistributionCustomerRankingPageResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomerranking/page")
    BaseResponse<DistributionCustomerRankingPageResponse> page(@RequestBody @Valid DistributionCustomerRankingPageRequest distributionCustomerRankingPageReq);


    /**
     * 查询排名信息
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomerranking/ranking")
    BaseResponse<DistributionCustomerRankingResponse> ranking(@RequestBody @Valid DistributionCustomerRankingPageRequest request);

}

