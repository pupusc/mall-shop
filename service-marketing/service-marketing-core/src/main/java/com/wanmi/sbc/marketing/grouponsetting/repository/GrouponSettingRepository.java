package com.wanmi.sbc.marketing.grouponsetting.repository;

import com.wanmi.sbc.marketing.grouponsetting.model.root.GrouponSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>拼团活动信息表DAO</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@Repository
public interface GrouponSettingRepository extends JpaRepository<GrouponSetting, String>,
        JpaSpecificationExecutor<GrouponSetting> {
	
}
