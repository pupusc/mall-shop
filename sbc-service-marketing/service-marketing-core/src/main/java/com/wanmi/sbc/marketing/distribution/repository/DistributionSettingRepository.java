package com.wanmi.sbc.marketing.distribution.repository;

import com.wanmi.sbc.marketing.distribution.model.DistributionSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>分销设置DAO</p>
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@Repository
public interface DistributionSettingRepository extends JpaRepository<DistributionSetting, String> {
	
}
