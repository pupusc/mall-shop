package com.wanmi.sbc.customer.api.provider.distribution.performance;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionMonthPerformanceListEnteringRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceCleanByTargetRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceEnteringRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>分销业绩业务操作Provider</p>
 * Created by of628-wenzhi on 2019-04-18-17:28.
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionPerformanceProvider")
public interface DistributionPerformanceProvider {

    /**
     * 根据指定年月清理数据
     *
     * @param request 清理条件参数 {@link DistributionPerformanceCleanByTargetRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distribution/performance/clean-by-target")
    BaseResponse cleanByTarget(@RequestBody @Valid DistributionPerformanceCleanByTargetRequest request);


    /**
     * 日分销业绩录入（不存在则创建，有则叠加预估收益和销售额）
     *
     * @param request 请求参数 {@link DistributionPerformanceEnteringRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distribution/performance/entering-performance")
    BaseResponse enteringPerformance(@RequestBody @Valid DistributionPerformanceEnteringRequest request);

    /**
     * 月分销员业绩批量录入
     *
     * @param request 请求参数 {@link DistributionMonthPerformanceListEnteringRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distribution/performance/entering-month-performance-list")
    BaseResponse enteringMonthPerformanceList(@RequestBody @Valid DistributionMonthPerformanceListEnteringRequest request);
}
