package com.wanmi.sbc.crm.scheduled;


import com.wanmi.sbc.crm.customerplanconversion.service.CustomerPlanConversionService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 运营计划转换效果定时任务
 *
 * @author zhangwenchang
 */
@JobHandler(value = "customerPlanningConversionScheduledGenerate")
@Component
public class CustomerPlanningConversionScheduledGenerate extends IJobHandler {

    @Autowired
    private CustomerPlanConversionService customerPlanConversionService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("执行运营计划转化效果统计开始");
        customerPlanConversionService.generator();
        XxlJobLogger.log("执行运营计划转化效果统计结束");
        return SUCCESS;
    }
}
