package com.wanmi.sbc.crm.test;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.crm.customerplan.service.CustomerPlanSendCountService;
import com.wanmi.sbc.crm.customerplan.service.CustomerPlanTaskService;
import com.wanmi.sbc.crm.customgroup.service.CustomGroupGenerateService;
import com.wanmi.sbc.crm.rfmgroupstatistics.service.RfmSystemGroupStatisticsService;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmCustomerDetailService;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmScoreStatisticService;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-14
 * \* Time: 18:34
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@RestController
@Slf4j
public class TestController {
    @Autowired
    private CustomGroupGenerateService customGroupGenerateService;
    @Autowired
    private CustomerPlanTaskService customerPlanTaskService;

    @Autowired
    private RfmCustomerDetailService rfmCustomerDetailService;
    @Autowired
    private RfmScoreStatisticService rfmScoreStatisticService;
    @Autowired
    private RfmSystemGroupStatisticsService rfmSystemGroupStatisticsService;

    @Autowired
    private CustomerPlanSendCountService customerPlanSendCountService;

    @GetMapping("/testJob/customGroupJob")
    public BaseResponse customGroupJob(String param,String statDate){
        LocalDate localDate = LocalDate.now();
        if(StringUtils.isNotEmpty(statDate)){
            localDate = DateUtil.parseDay(statDate).toLocalDate();
        }
        List<String> jobList = new ArrayList<>();
        if(StringUtils.isNotEmpty(param)){
            jobList.add(param);
        }else{
            jobList = Arrays.asList("1","2","3","4","5");
        }
        for(String job : jobList) {
            switch (job) {
                case "1":
                    this.customGroupGenerateService.generateCustomerBaseInfoStatistics();
                    log.info("会员基本信息统计完成！");
                    break;
                case "2":
                    this.customGroupGenerateService.generateCustomerRecentParamStatistics(localDate);
                    log.info("会员最近指标数据统计完成！");
                    break;
                case "3":
                    this.customGroupGenerateService.generateCustomerTradeStatistics(localDate);
                    log.info("会员订单数据统计完成！");
                    break;
                case "4":
                    this.customGroupGenerateService.generateCustomGroupCustomerRelStatistics();
                    log.info("自定义人群会员分组完成！");
                    break;
                case "5":
                    this.customGroupGenerateService.generateCustomGroupStatistics(localDate);
                    log.info("自定义人群人数统计完成！");
                    break;

            }
        }
        return BaseResponse.SUCCESSFUL();
    }
    @GetMapping("/testJob/rfmGroupJob")
    public BaseResponse rfmGroupJob(String param,String statDate){
        LocalDateTime localDatetime = LocalDateTime.now();
        if(StringUtils.isNotEmpty(statDate)){
            localDatetime = LocalDateTime.of(DateUtil.parseDay(statDate).toLocalDate(), LocalTime.now());
        }
        List<String> jobList = new ArrayList<>();
        if(StringUtils.isNotEmpty(param)){
            jobList.add(param);
        }else{
            jobList = Arrays.asList("1","2","3","4");
        }
        for(String job : jobList) {
            switch (job) {
                case "1":
                    this.rfmCustomerDetailService.generate(localDatetime);
                    log.info("会员rfm明细统计完成！");
                    break;
                case "2":
                    rfmScoreStatisticService.generate(DateUtil.format(localDatetime.minusDays(1), DateUtil.FMT_DATE_1));
                    log.info("rfm分数分布统计统计完成！");
                    break;
                case "3":
                    this.rfmScoreStatisticService.customerRfmGroupGenerate(DateUtil.format(localDatetime.minusDays(1), DateUtil.FMT_DATE_1));
                    log.info("会员明细rfm分群统计完成！");
                    break;
                case "4":
                    this.rfmSystemGroupStatisticsService.generate(localDatetime.minusDays(1));
                    log.info("rfm模型系统分群统计完成！");
                    break;

            }
        }

        return BaseResponse.SUCCESSFUL();
    }

    @GetMapping("/testJob/customerPlanJob")
    public BaseResponse customPlanJob(String param,String statDate){
        LocalDate localDate = LocalDate.now().minusDays(1);
        if(StringUtils.isNotEmpty(statDate)) {
            localDate = DateUtil.parseDay(statDate).toLocalDate();
        }
        this.customerPlanTaskService.generator(localDate);
        return BaseResponse.SUCCESSFUL();
    }


    @GetMapping("/testJob/customPlanCountJob")
    public BaseResponse customPlanCountJob(){
        customerPlanSendCountService.generator();
        return BaseResponse.SUCCESSFUL();
    }
}
