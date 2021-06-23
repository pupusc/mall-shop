package com.wanmi.sbc.distribute;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.performance.DistributionPerformanceQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceByDayQueryRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceByLast6MonthsQueryRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceSummaryQueryRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceYesterdayQueryRequest;
import com.wanmi.sbc.customer.api.response.distribution.performance.DistributionPerformanceByDayQueryResponse;
import com.wanmi.sbc.customer.api.response.distribution.performance.DistributionPerformanceByLast6MonthsQueryResponse;
import com.wanmi.sbc.customer.api.response.distribution.performance.DistributionPerformanceSummaryQueryResponse;
import com.wanmi.sbc.customer.api.response.distribution.performance.DistributionPerformanceYesterdayQueryResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceSummaryVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 分销员业绩统计
 *
 * @author liutao
 * @date 2019/4/19 10:15 AM
 */
@Api(tags = "DistributionPerformanceController", description = "分销员业绩统计")
@RestController
@RequestMapping("/distribution/performance")
@Validated
public class DistributionPerformanceController {

    @Autowired
    private DistributionPerformanceQueryProvider distributionPerformanceQueryProvider;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 查询日销售业绩
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "查询日销售业绩")
    @RequestMapping(value = "/day", method = RequestMethod.POST)
    public BaseResponse<DistributionPerformanceByDayQueryResponse> queryByDay(
            @RequestBody @Valid DistributionPerformanceByDayQueryRequest request) {
        return distributionPerformanceQueryProvider.queryByDay(request);
    }

    /**
     * 查询昨天的销售业绩
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "查询昨天的销售业绩")
    @RequestMapping(value = "/yesterday", method = RequestMethod.POST)
    public BaseResponse<DistributionPerformanceYesterdayQueryResponse> queryYesterday(
            @RequestBody @Valid DistributionPerformanceYesterdayQueryRequest request) {
        return distributionPerformanceQueryProvider.queryYesterday(request);
    }

    /**
     * 查询月销售业绩
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "查询月销售业绩")
    @RequestMapping(value = "/month", method = RequestMethod.POST)
    public BaseResponse<DistributionPerformanceByLast6MonthsQueryResponse> queryByLast6Months(
            @RequestBody @Valid DistributionPerformanceByLast6MonthsQueryRequest request) {
        return distributionPerformanceQueryProvider.queryByLast6Months(request);
    }

    /**
     * 查询当月截止昨天的分销业绩
     * @return
     */
    @ApiOperation(value = "查询当前月1号至昨天的业绩汇总")
    @RequestMapping(value = "/summary/month")
    public BaseResponse<DistributionPerformanceSummaryQueryResponse> summaryPerformanceCurrentMonth() {
        //客户id
        String customerId = commonUtil.getOperatorId();
        //分销员id
        String distributionId =
                distributionCustomerQueryProvider.getByCustomerId(new DistributionCustomerByCustomerIdRequest(customerId))
                        .getContext().getDistributionCustomerVO().getDistributionId();

        DistributionPerformanceSummaryQueryRequest request = DistributionPerformanceSummaryQueryRequest.builder()
                .distributionId(Collections.singletonList(distributionId))
                .startDate(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()))
                .endDate(LocalDate.now().minusDays(1L))
                .build();
        if(request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())){
            DistributionPerformanceSummaryQueryResponse response = new DistributionPerformanceSummaryQueryResponse();
            DistributionPerformanceSummaryVO distributionPerformanceSummaryVO = new DistributionPerformanceSummaryVO();
            distributionPerformanceSummaryVO.setCustomerId(customerId);
            distributionPerformanceSummaryVO.setDistributionId(distributionId);
            distributionPerformanceSummaryVO.setCommission(BigDecimal.ZERO);
            distributionPerformanceSummaryVO.setSaleAmount(BigDecimal.ZERO);
            response.setDataList(Arrays.asList(distributionPerformanceSummaryVO));
            return BaseResponse.success(response);
        }
        return distributionPerformanceQueryProvider.queryPerformanceSummary(request);
    }

}
