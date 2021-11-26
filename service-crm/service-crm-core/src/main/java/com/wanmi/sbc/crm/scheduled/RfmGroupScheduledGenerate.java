package com.wanmi.sbc.crm.scheduled;

import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.crm.customgroup.service.CustomGroupGenerateService;
import com.wanmi.sbc.crm.rfmgroupstatistics.service.RfmSystemGroupStatisticsService;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmCustomerDetailService;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmScoreStatisticService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@JobHandler(value = "rfmGroupJobHandler")
@Component
public class RfmGroupScheduledGenerate extends IJobHandler {

    @Autowired
    private RfmCustomerDetailService rfmCustomerDetailService;
    @Autowired
    private RfmScoreStatisticService rfmScoreStatisticService;
    @Autowired
    private RfmSystemGroupStatisticsService rfmSystemGroupStatisticsService;

    @Override
    public ReturnT<String> execute(String param) {
        XxlJobLogger.log("rfm系统人群会员统计开始");
        List<String> jobList = new ArrayList<>();
        if(StringUtils.isNotEmpty(param)){
            jobList.add(param);
        }else{
            jobList = Arrays.asList("1","2","3","4");
        }
        for(String job : jobList){
            generate(job);
        }

        return SUCCESS;
    }

    private void generate(String param){
        LocalDateTime localDatetime = LocalDateTime.now();
        switch (param){
            case "1":
                this.rfmCustomerDetailService.generate(localDatetime);
                XxlJobLogger.log("会员rfm明细统计完成！");
                return;
            case "2":
                rfmScoreStatisticService.generate(DateUtil.format(localDatetime.minusDays(1),DateUtil.FMT_DATE_1));
                XxlJobLogger.log("rfm分数分布统计统计完成！");
                return;
            case "3":
                this.rfmScoreStatisticService.customerRfmGroupGenerate(DateUtil.format(localDatetime.minusDays(1),DateUtil.FMT_DATE_1));
                XxlJobLogger.log("会员明细rfm分群统计完成！");
                return;
            case "4":
                this.rfmSystemGroupStatisticsService.generate(localDatetime.minusDays(1));
                XxlJobLogger.log( "rfm模型系统分群统计完成！");
                return;

        }
    }
}
