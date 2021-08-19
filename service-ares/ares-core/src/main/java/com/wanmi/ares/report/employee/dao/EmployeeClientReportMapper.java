package com.wanmi.ares.report.employee.dao;

import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.employee.model.entity.EmployeeClientQuery;
import com.wanmi.ares.report.employee.model.root.EmployeeClientReport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>业务员获客报表持久化Mapper</p>
 * Created by of628-wenzhi on 2017-10-19-下午4:27.
 */
@Repository
public interface EmployeeClientReportMapper {

    List<EmployeeClientReport> queryList(EmployeeClientQuery query);

    long countOfQuery(EmployeeClientQuery query);

    List<EmployeeClientReport> export(ExportQuery query);

    long countOfExport(ExportQuery query);

    int insertToday(List<EmployeeClientReport> list);

    int insertDate(List<EmployeeClientReport> list, LocalDate date);

    int insertYestDay(List<EmployeeClientReport> list);

    int insert7days(List<EmployeeClientReport> list);

    int insert30Days(List<EmployeeClientReport> list);

    int insertMonth(List<EmployeeClientReport> list);

    int clear7Days();

    int clear30Days();

    int clearMonth();

    int clearToday();

    int clearByDate(LocalDate date);

    int clearYestDay();

    int clearExpireByDay(String pname);

    int clearExpireByMonth(String pname);
}

