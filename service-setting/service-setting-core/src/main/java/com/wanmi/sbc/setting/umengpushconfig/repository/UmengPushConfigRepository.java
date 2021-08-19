package com.wanmi.sbc.setting.umengpushconfig.repository;

import com.wanmi.sbc.setting.umengpushconfig.model.root.UmengPushConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>友盟push接口配置DAO</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@Repository
public interface UmengPushConfigRepository extends JpaRepository<UmengPushConfig, Integer>,
        JpaSpecificationExecutor<UmengPushConfig> {

}
