package com.wanmi.sbc.marketing.distribution.repository;

import com.wanmi.sbc.marketing.distribution.model.DistributionRecruitGift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>礼包商品DAO</p>
 * @author gaomuwei
 * @date 2019-02-19 10:37:20
 */
@Repository
public interface DistributionRecruitGiftRepository extends JpaRepository<DistributionRecruitGift, String> {
	
}
