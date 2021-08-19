package com.wanmi.sbc.setting.businessconfig.repository;

import com.wanmi.sbc.setting.businessconfig.model.root.BusinessConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>招商页设置DAO</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@Repository
public interface BusinessConfigRepository extends JpaRepository<BusinessConfig, Integer>,
        JpaSpecificationExecutor<BusinessConfig> {

}
