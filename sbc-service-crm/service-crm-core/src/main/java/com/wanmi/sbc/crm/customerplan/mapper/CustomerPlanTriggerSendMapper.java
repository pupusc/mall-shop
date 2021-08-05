package com.wanmi.sbc.crm.customerplan.mapper;

import com.wanmi.sbc.crm.customerplan.model.CustomerPlanSend;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlanTriggerSend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPlanTriggerSendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerPlanTriggerSend record);

    CustomerPlanTriggerSend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerPlanTriggerSend record);

    int updateByPrimaryKey(CustomerPlanTriggerSend record);

    int insertSelect(CustomerPlanTriggerSend record);

    List<CustomerPlanSend> selectByType(CustomerPlanSend record);

    int statisticsPlanPushResult();

}