package com.wanmi.sbc.marketing.distribution.repository;

import com.wanmi.sbc.marketing.distribution.model.DistributionRewardCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>奖励优惠券DAO</p>
 * @author gaomuwei
 * @date 2019-02-19 10:40:20
 */
@Repository
public interface DistributionRewardCouponRepository extends JpaRepository<DistributionRewardCoupon, String> {
	
}
