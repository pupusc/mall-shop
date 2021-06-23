package com.wanmi.ares.report.employee.dao;

import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.employee.model.entity.EmployeePerformanceQuery;
import com.wanmi.ares.report.employee.model.root.EmployeePerformanceReport;
import com.wanmi.ares.report.trade.model.request.TradeCollect;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>业务员业绩报表持久化操作</p>
 * Created by of628-wenzhi on 2017-10-25-下午2:33.
 */
@Repository
public interface EmployeePerformanceReportMapper {

    List<EmployeePerformanceReport> queryList(EmployeePerformanceQuery query);

    List<EmployeePerformanceReport> export(ExportQuery export);

    long countOfExport(ExportQuery query);

    long countOfQuery(EmployeePerformanceQuery query);

    int insertToday(List<EmployeePerformanceReport> list);

    int testInsertToday(List<EmployeePerformanceReport> list);

    int insert7days(List<EmployeePerformanceReport> list);

    int insert30Days(List<EmployeePerformanceReport> list);

    int insertMonth(List<EmployeePerformanceReport> list);

    int clearToday();

    int testClearToday(LocalDate testCreateTime);
    
    int clear7Days();

    int clear30Days();

    int clearMonth();

    int clearExpireByDay(String pname);

    int clearExpireByMonth(String pname);

    List<EmployeePerformanceReport> collectThirdEmployeeTrade(TradeCollect computeDateIntervalDay);

    List<EmployeePerformanceReport> collectBossEmployeeTrade(TradeCollect computeDateIntervalDay);

    void deleteTodayDay(LocalDate now);

    void insertDay(List<EmployeePerformanceReport> list);

    void deleteRecentMonth(String oldYearMonth);
}
