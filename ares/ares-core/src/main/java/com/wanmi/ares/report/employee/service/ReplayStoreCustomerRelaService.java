package com.wanmi.ares.report.employee.service;


import com.wanmi.ares.report.employee.dao.EmployeeClientReportMapper;
import com.wanmi.ares.report.employee.dao.ReplayStoreCustomerRelaMapper;
import com.wanmi.ares.report.employee.model.root.EmployeeClientReport;
import com.wanmi.ares.report.employee.model.root.ReplayCustomerRela;
import com.wanmi.ares.report.employee.model.root.ReplayStoreCustomerRela;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReplayStoreCustomerRelaService {
    @Autowired
    ReplayStoreCustomerRelaMapper replayStoreCustomerRelaMapper;
    @Autowired
    EmployeeClientReportMapper employeeClientReportMapper;

    @Transactional
    public void generateDay(LocalDate today) {
        try {
            employeeClientReportMapper.clearByDate(today);
            Map<String, String> param = buildParam(today.atTime(LocalTime.MIN).toString(), today.atTime(LocalTime.MAX).toString()
                    , null);
            ReplayCustomerRela replayCustomerRela = buildRes(param);
            if (!(CollectionUtils.isEmpty(replayCustomerRela.getReplayBossCustomerRelas())
                    && CollectionUtils.isEmpty(replayCustomerRela.getReplayStoreCustomerRelas()))) {
                employeeClientReportMapper.insertDate(insertData(replayCustomerRela), today);
            }
        } catch (Exception e) {
            log.error("The [employee-performance] generate report  exception of today,\n", e);
        }
    }

    /**
     * 构建获客报表查询参数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private Map<String, String> buildParam(String startTime, String endTime, String employeeId) {
        Map<String, String> param = new HashMap<>();
        if (StringUtils.isNotEmpty(startTime)) {
            param.put("startTime", startTime);
        }
        if (StringUtils.isNotEmpty(endTime)) {
            param.put("endTime", endTime);
        }
        if (StringUtils.isNotEmpty(employeeId)) {
            param.put("employeeId", employeeId);
        }
        return param;
    }

    /**
     * 查询源数据
     *
     * @param param
     * @return
     */

    private ReplayCustomerRela buildRes(Map<String, String> param) {
        List<ReplayStoreCustomerRela> replayStoreCustomerRelas = replayStoreCustomerRelaMapper.selectTotal(param);
        List<Map<String, Object>> replayBossCustomerRelas = replayStoreCustomerRelaMapper.findTotal(param);
        return ReplayCustomerRela.builder()
                .replayStoreCustomerRelas(CollectionUtils.isEmpty(replayStoreCustomerRelas) ? Collections.emptyList() : replayStoreCustomerRelas)
                .replayBossCustomerRelas(CollectionUtils.isEmpty(replayBossCustomerRelas) ? Collections.emptyList() : replayBossCustomerRelas)
                .build();
    }

    /**
     * 插入统计数据
     *
     * @param replayCustomerRela
     * @return
     */
    private List<EmployeeClientReport> insertData(ReplayCustomerRela replayCustomerRela) {
        List<ReplayStoreCustomerRela> replayStoreCustomerRelas = replayCustomerRela.getReplayStoreCustomerRelas();
        List<EmployeeClientReport> storeCustomerRelas =
                replayStoreCustomerRelas.parallelStream().map(replayStoreCustomerRela -> {
                    String employeeId = replayStoreCustomerRela.getEmployeeId();
                    Map<String, String> param = buildParam(null, null, employeeId);
                    ReplayStoreCustomerRela rscr =
                            replayStoreCustomerRelaMapper.selectTotal(param).stream().findFirst().get();
                    return EmployeeClientReport.builder().companyId(String.valueOf(replayStoreCustomerRela.getCompanyInfoId()))
                            .employeeId(replayStoreCustomerRela.getEmployeeId())
                            .newlyNum(replayStoreCustomerRela.getTotal())
                            .total(rscr.getTotal())
                            .build();
                }).collect(Collectors.toList());
        List<Map<String, Object>> replayBossCustomerRelas = replayCustomerRela.getReplayBossCustomerRelas();
        List<EmployeeClientReport> bossCustomerRelas =
                replayBossCustomerRelas.stream().map(replayBossCustomerRela -> {
                    String employeeId = (String) replayBossCustomerRela.get("employee_id");

                    Map<String, String> param = buildParam(null, null, employeeId);
                    Map<String, Object> rscr =
                            replayStoreCustomerRelaMapper.findTotal(param).stream().findFirst().get();
                    return EmployeeClientReport.builder().companyId("0")
                            .employeeId(employeeId)
                            .newlyNum((Long) replayBossCustomerRela.get("total"))
                            .total((Long) (rscr.get("total")))
                            .build();
                }).collect(Collectors.toList());
        storeCustomerRelas.addAll(bossCustomerRelas);
        return storeCustomerRelas;
    }

    public void generateData(String param, LocalDate now) {
        if (StringUtils.isEmpty(param)) {
            return;
        }
        if (param.contains(String.valueOf(BigDecimal.ROUND_UP).trim())) {
            generateDay(now);
        }
        if (param.contains(String.valueOf(BigDecimal.ROUND_DOWN).trim())) {
            generateYesterday(now);
        }
        if (param.contains(String.valueOf(BigDecimal.ROUND_CEILING).trim())) {
            generateSeven(now);
        }
        if (param.contains(String.valueOf(BigDecimal.ROUND_FLOOR).trim())) {
            generateThirty(now);
        }
        if (param.contains(String.valueOf(BigDecimal.ROUND_HALF_UP).trim())) {
            generateMonth(now);
        }
    }

    @Transactional
    void generateMonth(LocalDate now) {
        LocalDate startDate = now.minusMonths(BigDecimal.ROUND_DOWN).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        employeeClientReportMapper.clearMonth();
        Map<String, String> param = buildParam(startDate.atTime(LocalTime.MIN).toString(),
                endDate.atTime(LocalTime.MAX).toString(),
                null);
        //统计上月1个月的数据
        ReplayCustomerRela replayCustomerRela = buildRes(param);
        if (!(CollectionUtils.isEmpty(replayCustomerRela.getReplayBossCustomerRelas())
                && CollectionUtils.isEmpty(replayCustomerRela.getReplayStoreCustomerRelas()))) {
            employeeClientReportMapper.insertMonth(insertData(replayCustomerRela));
        }

    }

    @Transactional
    void generateThirty(LocalDate now) {
        String day30 = now.minusDays(30).atTime(LocalTime.MIN).toString();
        employeeClientReportMapper.clear30Days();
        Map<String, String> param = buildParam(day30,
                now.minusDays(BigDecimal.ROUND_DOWN).atTime(LocalTime.MAX).toString(), null);
        ReplayCustomerRela replayCustomerRela = buildRes(param);
        if (!(CollectionUtils.isEmpty(replayCustomerRela.getReplayBossCustomerRelas())
                && CollectionUtils.isEmpty(replayCustomerRela.getReplayStoreCustomerRelas()))) {
            employeeClientReportMapper.insert30Days(insertData(replayCustomerRela));
        }
    }

    @Transactional
    void generateSeven(LocalDate now) {
        String day7 = now.minusDays(BigDecimal.ROUND_UNNECESSARY).atTime(LocalTime.MIN).toString();
        employeeClientReportMapper.clear7Days();
        Map<String, String> param = buildParam(day7,
                now.minusDays(BigDecimal.ROUND_DOWN).atTime(LocalTime.MAX).toString(), null);
        ReplayCustomerRela replayCustomerRela = buildRes(param);
        if (!(CollectionUtils.isEmpty(replayCustomerRela.getReplayBossCustomerRelas())
                && CollectionUtils.isEmpty(replayCustomerRela.getReplayStoreCustomerRelas()))) {
            employeeClientReportMapper.insert7days(insertData(replayCustomerRela));
        }

    }

    @Transactional
    void generateYesterday(LocalDate now) {
        LocalDate yesterday = now.minusDays(BigDecimal.ROUND_DOWN);
        employeeClientReportMapper.clearYestDay();
        Map<String, String> param = buildParam(yesterday.atTime(LocalTime.MIN).toString(), yesterday.atTime(LocalTime.MAX).toString()
                , null);
        ReplayCustomerRela replayCustomerRela = buildRes(param);
        if (!(CollectionUtils.isEmpty(replayCustomerRela.getReplayBossCustomerRelas())
                && CollectionUtils.isEmpty(replayCustomerRela.getReplayStoreCustomerRelas()))) {
            employeeClientReportMapper.insertYestDay(insertData(replayCustomerRela));
        }
    }

}
