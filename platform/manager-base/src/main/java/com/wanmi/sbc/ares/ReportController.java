package com.wanmi.sbc.ares;

import com.wanmi.ares.provider.ReportServiceProvider;
import com.wanmi.sbc.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunkun on 2017/10/19.
 */
@Api(tags = "ReportController", description = "报表 Api")
@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportServiceProvider reportServiceProvider;

    @ApiOperation(value = "生成报表")
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public BaseResponse generateReport(String data) {
        try {
            reportServiceProvider.generateReport(data);
            return BaseResponse.SUCCESSFUL();
        } catch (Exception ex) {
            log.error("生成报表异常,", ex);
        }
        return BaseResponse.FAILED();
    }

    @ApiOperation(value = "订单报表生成")
    @RequestMapping(value = "/trade_generate", method = RequestMethod.POST)
    public BaseResponse tradeGenerateReport(String data) {
        try {
            reportServiceProvider.tradeGenerateReport(data);
            return BaseResponse.SUCCESSFUL();
        } catch (Exception ex) {
            log.error("订单报表生成异常,", ex);
        }
        return BaseResponse.FAILED();
    }

    @ApiOperation(value = "生成当日报表")
    @RequestMapping(value = "/generateToday", method = RequestMethod.GET)
    public BaseResponse generateTodayReport() {
        try {
            reportServiceProvider.generateTodayReport();
            return BaseResponse.SUCCESSFUL();
        } catch (Exception ex) {
            log.error("生成当日报表异常,", ex);
        }
        return BaseResponse.FAILED();
    }

}
