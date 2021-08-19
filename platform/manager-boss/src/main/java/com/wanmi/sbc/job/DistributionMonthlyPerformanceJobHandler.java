package com.wanmi.sbc.job;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.performance.DistributionPerformanceProvider;
import com.wanmi.sbc.customer.api.provider.distribution.performance.DistributionPerformanceQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistinctDistributionIdsQueryRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionMonthPerformanceListEnteringRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceCleanByTargetRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceSummaryQueryRequest;
import com.wanmi.sbc.customer.bean.dto.DistributionMonthPerformanceDTO;
import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceSummaryVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>分销业绩月报表生成，暂配置为每月1日凌晨3点执行</p>
 * Created by of628-wenzhi on 2019-04-24-16:04.
 */
@Component
@JobHandler("distributionMonthlyPerformanceJobHandler")
@Slf4j
public class DistributionMonthlyPerformanceJobHandler extends IJobHandler {

    @Autowired
    private DistributionPerformanceQueryProvider queryProvider;

    @Autowired
    private DistributionPerformanceProvider provider;

    private static final int FETCH_SIZE = 200;

    @Override
    public ReturnT<String> execute(String args) {
        XxlJobLogger.log("========= Monthly distribution performance generation start... =========");
        //清理过期的分销数据
        clear();
        //录入月分销业绩数据
        int total = generate(args);
        XxlJobLogger.log("This task generates {} performance records", total);
        XxlJobLogger.log("========= Monthly distribution performance has been generation completed... =========");
        return ReturnT.SUCCESS;
    }

    private void clear() {
        LocalDate clearDate = LocalDate.now().minusMonths(7);
        DistributionPerformanceCleanByTargetRequest request = new DistributionPerformanceCleanByTargetRequest();
        request.setYear(clearDate.getYear());
        request.setMonth(clearDate.getMonthValue());
        try {
            provider.cleanByTarget(request);
            XxlJobLogger.log("This task clean performance data by date completed,request={}", request);
        } catch (Exception e) {
            //清理失败不中断任务，确保数据生成不受影响
            XxlJobLogger.log("This task clean performance data by date error,request={},", request, e);
        }
    }

    private int generate(String args) {
        LocalDate startDate;
        LocalDate endDate;//业务需求只统计上个月的，测试场景下暂设可配
        if (args.isEmpty()) {
            startDate = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            endDate = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        } else {
            startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
            endDate = LocalDate.now();
        }
        DistinctDistributionIdsQueryRequest request = new DistinctDistributionIdsQueryRequest();
        request.setPageSize(FETCH_SIZE);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        int pageNum = 0;
        int total = 0;
        while (true) {
            request.setPageNum(pageNum);
            //去重抓取指定日期范围内分销员id
            List<String> distributionIds =
                    queryProvider.queryDistinctDistributionIds(request).getContext().getDistributionIds();
            if (CollectionUtils.isEmpty(distributionIds)) {
                break;
            }
            //获取分销员指定日期范围内的分销业绩
            DistributionPerformanceSummaryQueryRequest summaryRequest =
                    new DistributionPerformanceSummaryQueryRequest();
            summaryRequest.setDistributionId(distributionIds);
            summaryRequest.setStartDate(startDate);
            summaryRequest.setEndDate(endDate);
            List<DistributionPerformanceSummaryVO> records =
                    queryProvider.queryPerformanceSummary(summaryRequest).getContext().getDataList();
            if (Collections.isEmpty(records)) {
                break;
            }
            List<DistributionMonthPerformanceDTO> monthPerformanceDTOList = records.stream().map((i -> {
                DistributionMonthPerformanceDTO dto = new DistributionMonthPerformanceDTO();
                KsBeanUtil.copyPropertiesThird(i, dto);
                dto.setTargetDate(endDate);
                return dto;
            })).collect(Collectors.toList());
            DistributionMonthPerformanceListEnteringRequest enteringRequest =
                    new DistributionMonthPerformanceListEnteringRequest(monthPerformanceDTOList);
            //插入分销记录
            provider.enteringMonthPerformanceList(enteringRequest);
            total += monthPerformanceDTOList.size();
            pageNum++;
        }
        return total;
    }

}
