package com.wanmi.sbc.customer.api.provider.distribution.performance;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.performance.*;
import com.wanmi.sbc.customer.api.response.distribution.performance.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * <p>分销员业绩报表查询Provider</p>
 * Created by of628-wenzhi on 2019-04-17-18:43.
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionPerformanceQueryProvider")
public interface DistributionPerformanceQueryProvider {

    /**
     * 按天查询指定分销员分销业绩数据（日维度统计）
     *
     * @param request 请求参数 {@link DistributionPerformanceByDayQueryRequest}
     * @return 业绩数据 {@link DistributionPerformanceByDayQueryResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distribution/performance/query-by-day")
    BaseResponse<DistributionPerformanceByDayQueryResponse> queryByDay(
            @RequestBody @Valid DistributionPerformanceByDayQueryRequest request);

    /**
     * 查询分销员昨日分销业绩（日维度统计）
     * @param request 请求参数 {@link DistributionPerformanceYesterdayQueryRequest}
     * @return 业绩数据 {@link DistributionPerformanceYesterdayQueryResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distribution/performance/query-yesterday")
    BaseResponse<DistributionPerformanceYesterdayQueryResponse> queryYesterday(@RequestBody @Valid DistributionPerformanceYesterdayQueryRequest request);

    /**
     * 查询指定分销员最近6个月的分销业绩数据（月维度统计）
     *
     * @param request 请求参数 {@link DistributionPerformanceByLast6MonthsQueryRequest}
     * @return 业绩数据 {@link DistributionPerformanceByLast6MonthsQueryResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distribution/performance/query-by-last-6months")
    BaseResponse<DistributionPerformanceByLast6MonthsQueryResponse> queryByLast6Months(
            @RequestBody @Valid DistributionPerformanceByLast6MonthsQueryRequest request);

    /**
     * 按日期范围分批查询去重后有分销业绩的分销员id集合
     *
     * @param request 只包含分页参数的请求 {@link BaseQueryRequest}
     * @return 业绩数据 {@link DistinctDistributionIdsQueryResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distribution/performance/query-distinct-distribution-ids")
    BaseResponse<DistinctDistributionIdsQueryResponse> queryDistinctDistributionIds(
            @RequestBody @Valid DistinctDistributionIdsQueryRequest request);

    /**
     * 汇总指定分销员指定日期范围内的业绩数据
     *
     * @param request 汇总请求参数 {@link DistributionPerformanceSummaryQueryRequest}
     * @return 汇总数据 {@link DistributionPerformanceSummaryQueryResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distribution/performance/query-performance-summary")
    BaseResponse<DistributionPerformanceSummaryQueryResponse> queryPerformanceSummary(
            @RequestBody @Valid DistributionPerformanceSummaryQueryRequest request);
}
