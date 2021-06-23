package com.wanmi.ares.scheduled.employee;

import com.wanmi.ares.report.employee.service.EmployeeReportGenerateService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-08-27 11:39
 */
@JobHandler(value = "employeeReportScheduledGenerate")
@Component
public class EmployeeReportScheduledGenerate extends IJobHandler {

    @Resource
    private EmployeeReportGenerateService employeeReportGenerateService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        employeeReportGenerateService.generateData(param,LocalDate.now());
        return SUCCESS;
    }
}
