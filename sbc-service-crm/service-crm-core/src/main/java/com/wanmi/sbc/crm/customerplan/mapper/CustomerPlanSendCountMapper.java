package com.wanmi.sbc.crm.customerplan.mapper;

import com.wanmi.sbc.crm.customerplan.model.CustomerPlanSendCount;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPlanSendCountMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByPlanId(Long planId);

    int insert(CustomerPlanSendCount record);

    int insertSelective(CustomerPlanSendCount record);

    CustomerPlanSendCount selectByPrimaryKey(Long id);

    CustomerPlanSendCount selectByPlanId(Long planId);

    int updateByPrimaryKeySelective(CustomerPlanSendCount record);

    int updateByPrimaryKey(CustomerPlanSendCount record);

    int deleteAll();
}