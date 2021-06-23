package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingAddRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingInitRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingModifyRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingAddResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingInitResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>用户分销排行榜保存服务Provider</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionCustomerRankingSaveProvider")
public interface DistributionCustomerRankingSaveProvider {

    /**
     * 新增用户分销排行榜API
     * @param distributionCustomerRankingAddRequest 用户分销排行榜新增参数结构 {@link DistributionCustomerRankingAddRequest}
     * @return 新增的用户分销排行榜信息 {@link DistributionCustomerRankingAddResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomerranking/add")
    BaseResponse<DistributionCustomerRankingAddResponse> add(@RequestBody @Valid DistributionCustomerRankingAddRequest distributionCustomerRankingAddRequest);

    /**
     * 修改用户分销排行榜API
     * @param distributionCustomerRankingModifyRequest 用户分销排行榜修改参数结构 {@link DistributionCustomerRankingModifyRequest}
     * @return 修改的用户分销排行榜信息 {@link DistributionCustomerRankingModifyResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomerranking/modify")
    BaseResponse<DistributionCustomerRankingModifyResponse> modify(@RequestBody @Valid DistributionCustomerRankingModifyRequest distributionCustomerRankingModifyRequest);


    /**
     * 初始化用户分销排行榜API
     * @return 用户分销排行榜信息 {@link DistributionCustomerRankingInitResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomerranking/init-ranking-data")
    BaseResponse<DistributionCustomerRankingInitResponse> initRankingData(@RequestBody @Valid DistributionCustomerRankingInitRequest request);
}

