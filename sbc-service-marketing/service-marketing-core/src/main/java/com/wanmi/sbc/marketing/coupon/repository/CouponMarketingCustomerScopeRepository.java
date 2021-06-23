package com.wanmi.sbc.marketing.coupon.repository;

import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingCustomerScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>优惠券活动目标客户作用范围DAO</p>
 * @author lq
 * @date 2019-08-02 14:50:57
 */
@Repository
public interface CouponMarketingCustomerScopeRepository extends JpaRepository<CouponMarketingCustomerScope, String>,
        JpaSpecificationExecutor<CouponMarketingCustomerScope> {
    /**
     * 删除活动id目标客户作用范围
     */
    int deleteByActivityId(String activityId);

    /**
     * 根据活动id目标客户作用范围
     * @param activityId 活动id
     * @return
     */
    List<CouponMarketingCustomerScope> findByActivityId(String activityId);

}
