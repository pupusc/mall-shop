package com.wanmi.sbc.crm.scheduled;

import com.wanmi.sbc.crm.customgroup.service.CustomGroupGenerateService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@JobHandler(value = "customGroupJobHandler")
@Component
@Slf4j
public class CustomGroupScheduledGenerate extends IJobHandler {

    @Autowired
    private CustomGroupGenerateService customGroupGenerateService;

    /**
     * cron = "0 0 1 * * ?"
     * @param param
     * @return
     */
    @Override
    public ReturnT<String> execute(String param) {
        XxlJobLogger.log("自定义人群会员统计开始");
        log.info("自定义人群会员统计开始");
        List<String> jobList = new ArrayList<>();
        if(StringUtils.isNotEmpty(param)){
            jobList.add(param);
        }else{
            jobList = Arrays.asList("1","2","3","4","5");
        }
        for(String job : jobList){
            generate(job);
        }
        log.info("自定义人群会员统计结束");
        return SUCCESS;
    }

    private void generate(String param){
        LocalDate localDate = LocalDate.now();
        switch (param){
            case "1":
                this.customGroupGenerateService.generateCustomerBaseInfoStatistics();
                XxlJobLogger.log("会员基本信息统计完成！");
                return;
            case "2":
                this.customGroupGenerateService.generateCustomerRecentParamStatistics(localDate);
                XxlJobLogger.log("会员最近指标数据统计完成！");
                return;
            case "3":
                this.customGroupGenerateService.generateCustomerTradeStatistics(localDate);
                XxlJobLogger.log("会员订单数据统计完成！");
                return;
            case "4":
                this.customGroupGenerateService.generateCustomGroupCustomerRelStatistics();
                XxlJobLogger.log( "自定义人群会员分组完成！");
                return;
            case "5":
                this.customGroupGenerateService.generateCustomGroupStatistics(localDate);
                XxlJobLogger.log( "自定义人群人数统计完成！");
                return;
        }
    }
}
