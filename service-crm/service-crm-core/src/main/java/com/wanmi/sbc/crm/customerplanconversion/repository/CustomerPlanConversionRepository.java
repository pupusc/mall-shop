package com.wanmi.sbc.crm.customerplanconversion.repository;

import com.wanmi.sbc.crm.customerplanconversion.model.root.CustomerPlanConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * <p>运营计划转化效果DAO</p>
 *
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@Repository
public interface CustomerPlanConversionRepository extends JpaRepository<CustomerPlanConversion, Long>,
        JpaSpecificationExecutor<CustomerPlanConversion> {

    @Modifying
    @Query("delete from CustomerPlanConversion c where c.planId = ?1")
    int deleteByPlanId(Long planId);

    CustomerPlanConversion findTopByPlanId(Long planId);
}
