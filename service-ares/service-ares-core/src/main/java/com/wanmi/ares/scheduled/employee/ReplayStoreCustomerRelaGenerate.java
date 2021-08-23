package com.wanmi.ares.scheduled.employee;


import com.wanmi.ares.report.employee.service.ReplayStoreCustomerRelaService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@JobHandler(value = "ReplayStoreCustomerRelaGenerate")
@Component
public class ReplayStoreCustomerRelaGenerate extends IJobHandler {
    @Autowired
    ReplayStoreCustomerRelaService replayStoreCustomerRelaService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        replayStoreCustomerRelaService.generateData(param, LocalDate.now());
        return SUCCESS;
    }
}
