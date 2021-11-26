package com.wanmi.sbc.crm.customerplansms.repository;

import com.wanmi.sbc.crm.customerplansms.model.root.CustomerPlanSmsRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>运营计划与短信关联DAO</p>
 * @author dyt
 * @date 2020-01-10 11:12:50
 */
@Repository
public interface CustomerPlanSmsRelRepository extends JpaRepository<CustomerPlanSmsRel, Long>,
        JpaSpecificationExecutor<CustomerPlanSmsRel> {

    void deleteByPlanId(Long planId);

    CustomerPlanSmsRel findByPlanId(Long planId);
}
