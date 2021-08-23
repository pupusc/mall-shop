package com.wanmi.ares.thrift;

import com.wanmi.ares.interfaces.ReportService;
import com.wanmi.ares.report.trade.service.TradeReportService;
import com.wanmi.ares.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by sunkun on 2017/10/19.
 */
@Service
public class ReportServiceImpl implements ReportService.Iface{


    @Override
    public void generateReport(String date) {
        LocalDate localDate = DateUtil.parse2Date(date, DateUtil.FMT_DATE_1);
//        reportTask.generate(localDate);
    }

    @Override
    public void tradeGenerateReport(String date) {
        LocalDate localDate = DateUtil.parse2Date(date, DateUtil.FMT_DATE_1);
//        reportTask.tradeGenerate(localDate);
    }

    @Override
    public void generateTodayReport() {
//        reportTask.generateToday();
    }
}
