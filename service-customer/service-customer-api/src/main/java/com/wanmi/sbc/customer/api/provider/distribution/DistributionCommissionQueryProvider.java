package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCommissionExportRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCommissionPageRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCommissionExportResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCommissionPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Created by feitingting on 2019/2/26.
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionCommissionQueryProvider")
public interface DistributionCommissionQueryProvider {
    /**
     * 根据条件分页查询分销员佣金
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/commission/page")
    BaseResponse<DistributionCommissionPageResponse> findDistributionCommissionPage(@RequestBody @Valid DistributionCommissionPageRequest request);


    /**
     * 根据条件导出查询分销员佣金
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/commission/export")
    BaseResponse<DistributionCommissionExportResponse> findDistributionCommissionExport(@RequestBody @Valid DistributionCommissionExportRequest request);

    /**
     * 分销员佣金Redis初始化
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/commission/init")
    BaseResponse initStatisticsCache();
}
