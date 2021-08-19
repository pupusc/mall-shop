package com.wanmi.ares.scheduled.trade;

import com.wanmi.ares.report.trade.service.TradeReportService;
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
@JobHandler(value = "tradeReportScheduledGenerateData")
@Component
public class TradeReportScheduledGenerateData extends IJobHandler {

    @Resource
    private TradeReportService tradeReportService;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        tradeReportService.generateData(param,LocalDate.now());
        return SUCCESS;
    }
}
