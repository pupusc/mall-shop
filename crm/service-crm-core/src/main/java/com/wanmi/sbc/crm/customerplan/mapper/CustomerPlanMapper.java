package com.wanmi.sbc.crm.customerplan.mapper;

import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanPageRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanListRequest;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPlanMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerPlan record);

    CustomerPlan selectByPrimaryKey(Long id);

    List<CustomerPlan> selectByPage(CustomerPlanPageRequest queryReq);

    long countByPageTotal(CustomerPlanPageRequest queryReq);

    List<CustomerPlan> selectByCondition(CustomerPlanListRequest queryReq);

    int updateByPrimaryKeySelective(CustomerPlan record);

    int updateByPrimaryKey(CustomerPlan record);

    List<CustomerPlan> selectTaskByPage(CustomerPlanPageRequest queryReq);

    List<Long> selectPlanningIds();

    Long selectSumGiftCount(Long planId);
}