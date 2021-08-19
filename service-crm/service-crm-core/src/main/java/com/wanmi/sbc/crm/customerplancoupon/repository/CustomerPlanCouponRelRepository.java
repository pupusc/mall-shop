package com.wanmi.sbc.crm.customerplancoupon.repository;

import com.wanmi.sbc.crm.customerplancoupon.model.root.CustomerPlanCouponRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>运营计划与优惠券关联DAO</p>
 * @author dyt
 * @date 2020-01-08 14:11:18
 */
@Repository
public interface CustomerPlanCouponRelRepository extends JpaRepository<CustomerPlanCouponRel, Long>,
        JpaSpecificationExecutor<CustomerPlanCouponRel> {

    /**
     * 根据计划id批量获取优惠券信息
     *
     * @param planIds 计划id
     * @return 优惠券信息
     */
    List<CustomerPlanCouponRel> findAllByPlanIdIn(List<Long> planIds);

    /**
     * 根据计划id删除优惠券信息
     *
     * @param planId 计划id
     */
    int deleteByPlanId(Long planId);
}
