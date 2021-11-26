package com.wanmi.sbc.crm.customerplanapppush.repository;

import com.wanmi.sbc.crm.customerplanapppush.model.root.CustomerPlanAppPushRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>运营计划App通知DAO</p>
 * @author dyt
 * @date 2020-01-10 11:14:29
 */
@Repository
public interface CustomerPlanAppPushRelRepository extends JpaRepository<CustomerPlanAppPushRel, Long>,
        JpaSpecificationExecutor<CustomerPlanAppPushRel> {

    void deleteByPlanId(Long planId);

    CustomerPlanAppPushRel findByPlanId(Long planId);
}
