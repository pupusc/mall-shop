package com.wanmi.ares.scheduled.customer;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName ReplayCustomerOrderGenerate
 * @Description 客户统计客户客户订货报表统计
 * @Author lvzhenwei
 * @Date 2019/9/23 11:40
 **/
@JobHandler(value = "reportCustomerGenerate")
@Component
public class ReportCustomerGenerate extends IJobHandler {

    @Resource
    private CustomerDataStatisticsService customerOrderDataStatisticsService;

    @Override
    public ReturnT<String> execute(String type) throws Exception {
        customerOrderDataStatisticsService.generateCustomerOrderData(type);
        return SUCCESS;
    }
}
