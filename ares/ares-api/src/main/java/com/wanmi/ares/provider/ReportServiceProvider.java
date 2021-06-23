package com.wanmi.ares.provider;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:15
 */
@FeignClient(name = "${application.ares.name}", contextId = "ReportServiceProvider")
public interface ReportServiceProvider {
    @PostMapping("/ares/${application.ares.version}/report/generateReport")
    void generateReport(@RequestBody @Valid String date);

    @PostMapping("/ares/${application.ares.version}/report/tradeGenerateReport")
    void tradeGenerateReport(@RequestBody @Valid String date);

    @PostMapping("/ares/${application.ares.version}/report/generateTodayReport")
    void generateTodayReport();

}
