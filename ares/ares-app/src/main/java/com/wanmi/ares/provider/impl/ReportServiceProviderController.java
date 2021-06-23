package com.wanmi.ares.provider.impl;

import com.wanmi.ares.provider.ReportServiceProvider;
import com.wanmi.ares.thrift.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:17
 */
@RestController
public class ReportServiceProviderController implements ReportServiceProvider {

    @Autowired
    private ReportServiceImpl reportService;

    @Override
    public void generateReport(@RequestBody @Valid String date) {
        reportService.generateReport(date);
    }

    @Override
    public void tradeGenerateReport(@RequestBody @Valid String date) {
        reportService.tradeGenerateReport(date);
    }

    @Override
    public void generateTodayReport() {
        reportService.generateTodayReport();
    }
}
