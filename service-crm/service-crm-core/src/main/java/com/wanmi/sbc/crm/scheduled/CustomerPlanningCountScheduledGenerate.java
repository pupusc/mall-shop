package com.wanmi.sbc.crm.scheduled;


import com.wanmi.sbc.crm.customerplan.service.CustomerPlanSendCountService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "customerPlanningCountJobHandler")
@Component
public class CustomerPlanningCountScheduledGenerate extends IJobHandler {

    @Autowired
    private CustomerPlanSendCountService customerPlanSendCountService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("执行运营计划权益统计开始");
        customerPlanSendCountService.generator();
        XxlJobLogger.log("执行运营计划权益统计结束");
        return SUCCESS;
    }
}
