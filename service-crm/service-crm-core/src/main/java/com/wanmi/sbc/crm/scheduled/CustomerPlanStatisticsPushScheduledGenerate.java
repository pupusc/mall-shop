package com.wanmi.sbc.crm.scheduled;

import com.wanmi.sbc.crm.customerplan.service.CustomerPlanStatisticsPushTaskService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName CustomerPlanStatisticsPushScheduledGenerate
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/2/5 14:55
 **/
@JobHandler(value = "customerPlanStatisticsPushJobHandler")
@Component
public class CustomerPlanStatisticsPushScheduledGenerate extends IJobHandler {

    @Autowired
    private CustomerPlanStatisticsPushTaskService customerPlanStatisticsPushTaskService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("执行运营计划通知人次统计开始");
        customerPlanStatisticsPushTaskService.statisticsPlanPushResult();
        XxlJobLogger.log("执行运营计划通知人次统计结束");
        return SUCCESS;
    }
}
