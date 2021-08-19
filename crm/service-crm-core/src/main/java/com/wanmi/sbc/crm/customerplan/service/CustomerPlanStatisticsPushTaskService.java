package com.wanmi.sbc.crm.customerplan.service;

import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanTriggerSendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName CustomerPlanStatisticsTaskService
 * @Description 运营计划效果统计
 * @Author lvzhenwei
 * @Date 2020/2/5 14:02
 **/
@Service
public class CustomerPlanStatisticsPushTaskService {

    @Autowired
    private CustomerPlanTriggerSendMapper customerPlanTriggerSendMapper;

    public void statisticsPlanPushResult(){
        customerPlanTriggerSendMapper.statisticsPlanPushResult();
    }

}
