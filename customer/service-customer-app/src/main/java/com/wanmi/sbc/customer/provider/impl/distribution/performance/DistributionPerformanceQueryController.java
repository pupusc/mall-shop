package com.wanmi.sbc.customer.provider.impl.distribution.performance;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.enums.DateCycleEnum;
import com.wanmi.sbc.customer.api.provider.distribution.performance.DistributionPerformanceQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.performance.*;
import com.wanmi.sbc.customer.api.response.distribution.performance.*;
import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceByDayVO;
import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceByMonthVO;
import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceSummaryVO;
import com.wanmi.sbc.customer.distribution.performance.model.entity.DistinctDistributionIdsQuery;
import com.wanmi.sbc.customer.distribution.performance.model.entity.PerformanceQuery;
import com.wanmi.sbc.customer.distribution.performance.model.entity.PerformanceTotal;
import com.wanmi.sbc.customer.distribution.performance.model.entity.PerformanceTotalQuery;
import com.wanmi.sbc.customer.distribution.performance.model.root.DistributionPerformanceDay;
import com.wanmi.sbc.customer.distribution.performance.model.root.DistributionPerformanceMonth;
import com.wanmi.sbc.customer.distribution.performance.service.DistributionPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>分销员业绩查询Controller</p>
 * Created by of628-wenzhi on 2019-04-18-10:50.
 */
@RestController
@Validated
public class DistributionPerformanceQueryController implements DistributionPerformanceQueryProvider {

    @Autowired
    private DistributionPerformanceService performanceService;

    @Override
    public BaseResponse<DistributionPerformanceByDayQueryResponse> queryByDay(
            @RequestBody @Valid DistributionPerformanceByDayQueryRequest request) {
        PerformanceQuery query = PerformanceQuery.builder()
                .startDate(getStartDate(request.getDateCycleEnum(), request.getYear(), request.getMonth()))
                .endDate(getEndDate(request.getDateCycleEnum(), request.getYear(), request.getMonth()))
                .distributionId(request.getDistributionId())
                .build();
        List<DistributionPerformanceDay> records = performanceService.findByTargetDate(query);
        List<DistributionPerformanceByDayVO> dataList = wraperListByDayFromData2Vo(records);
        BigDecimal totalSaleAmount = statisticsAmount(dataList.stream().map(DistributionPerformanceByDayVO
                ::getSaleAmount));
        BigDecimal totalCommission = statisticsAmount(dataList.stream().map(DistributionPerformanceByDayVO
                ::getCommission));
        return BaseResponse.success(new DistributionPerformanceByDayQueryResponse(dataList, totalSaleAmount,
                totalCommission));
    }

    @Override
    public BaseResponse<DistributionPerformanceYesterdayQueryResponse> queryYesterday(
            @RequestBody @Valid DistributionPerformanceYesterdayQueryRequest request) {
        PerformanceQuery query = PerformanceQuery.builder()
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().minusDays(1))
                .distributionId(request.getDistributionId())
                .build();
        List<DistributionPerformanceDay> records = performanceService.findByTargetDate(query);
        DistributionPerformanceByDayVO vo = new DistributionPerformanceByDayVO();
        if (!records.isEmpty()) {
            KsBeanUtil.copyProperties(records.get(0), vo);
        }
        return BaseResponse.success(new DistributionPerformanceYesterdayQueryResponse(vo));
    }

    @Override
    public BaseResponse<DistributionPerformanceByLast6MonthsQueryResponse> queryByLast6Months(
            @RequestBody @Valid DistributionPerformanceByLast6MonthsQueryRequest request) {
        List<DistributionPerformanceMonth> records = performanceService.findByLast6Months(request.getDistributionId());
        List<DistributionPerformanceByMonthVO> dataList = wraperListByMonthFromData2Vo(records);
        BigDecimal totalSaleAmount = statisticsAmount(dataList.stream().map(DistributionPerformanceByMonthVO
                ::getSaleAmount));
        BigDecimal totalCommission = statisticsAmount(dataList.stream().map(DistributionPerformanceByMonthVO
                ::getCommission));
        return BaseResponse.success(new DistributionPerformanceByLast6MonthsQueryResponse(dataList, totalSaleAmount,
                totalCommission));
    }

    @Override
    public BaseResponse<DistinctDistributionIdsQueryResponse> queryDistinctDistributionIds(
            @RequestBody @Valid DistinctDistributionIdsQueryRequest request) {
        List<String> ids = performanceService.fetchDistinctDistributionIds(new DistinctDistributionIdsQuery(
                request.getPageable(), request.getStartDate(), request.getEndDate()));
        return BaseResponse.success(new DistinctDistributionIdsQueryResponse(ids));
    }

    @Override
    public BaseResponse<DistributionPerformanceSummaryQueryResponse> queryPerformanceSummary(
            @RequestBody @Valid DistributionPerformanceSummaryQueryRequest request) {
        PerformanceTotalQuery query = new PerformanceTotalQuery();
        KsBeanUtil.copyPropertiesThird(request, query);
        List<PerformanceTotal> performanceTotals = performanceService.queryTotalPerformanceByDistributionIds(query);
        List<DistributionPerformanceSummaryVO> dataList = wraperSummaryListByDayFromData2Vo(performanceTotals);
        return BaseResponse.success(new DistributionPerformanceSummaryQueryResponse(dataList));
    }

    private List<DistributionPerformanceSummaryVO> wraperSummaryListByDayFromData2Vo(List<PerformanceTotal> records) {
        return records.stream().map(i -> {
            DistributionPerformanceSummaryVO vo = new DistributionPerformanceSummaryVO();
            KsBeanUtil.copyPropertiesThird(i, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    private BigDecimal statisticsAmount(Stream<BigDecimal> amountStream) {
        return amountStream.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_DOWN);
    }


    private LocalDate getStartDate(DateCycleEnum cycleEnum, Integer year, Integer month) {
        switch (cycleEnum) {
            case LATEST7DAYS:
                return LocalDate.now().minusDays(7);
            case LATEST30DAYS:
                return LocalDate.now().minusDays(31);
            case MONTH:
                return LocalDate.of(year, month, 1);
        }
        return LocalDate.now().minusDays(7);
    }

    private static LocalDate getEndDate(DateCycleEnum cycleEnum, Integer year, Integer month) {
        if (cycleEnum == DateCycleEnum.MONTH) {
            return LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
        }
        return LocalDate.now().minusDays(1);
    }

    private List<DistributionPerformanceByDayVO> wraperListByDayFromData2Vo(List<DistributionPerformanceDay> records) {
        return records.stream().map(i -> {
            DistributionPerformanceByDayVO vo = new DistributionPerformanceByDayVO();
            KsBeanUtil.copyProperties(i, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    private List<DistributionPerformanceByMonthVO> wraperListByMonthFromData2Vo(List<DistributionPerformanceMonth> records) {
        return records.stream().map(i -> {
            DistributionPerformanceByMonthVO vo = new DistributionPerformanceByMonthVO();
            KsBeanUtil.copyProperties(i, vo);
            vo.setTargetMonth(i.getTargetDate().getYear() + "-" + i.getTargetDate().getMonthValue());
            return vo;
        }).collect(Collectors.toList());
    }

}
