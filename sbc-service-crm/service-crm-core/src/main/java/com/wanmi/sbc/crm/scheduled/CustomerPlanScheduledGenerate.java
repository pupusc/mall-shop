package com.wanmi.sbc.crm.scheduled;

import com.wanmi.sbc.crm.customerplan.model.CustomerPlanTriggerSend;
import com.wanmi.sbc.crm.customerplan.service.CustomerPlanTaskService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * \* Author: zgl
 * \* Date: 2020-1-13
 * \* Time: 15:16
 * \* Description:
 * \
 */
@JobHandler(value = "customerPlanJobHandler")
@Component
public class CustomerPlanScheduledGenerate extends IJobHandler {

    @Autowired
    private CustomerPlanTaskService taskService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("执行运营计划权益发放开始");
        this.taskService.generator(LocalDate.now().minusDays(1));
        XxlJobLogger.log("执行运营计划权益发放结束");
        return SUCCESS;
    }
}
