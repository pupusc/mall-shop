package com.wanmi.sbc.setting.push.repository;

import com.wanmi.sbc.setting.push.model.root.AppPushConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>消息推送DAO</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@Repository
public interface AppPushConfigRepository extends JpaRepository<AppPushConfig, Long>,
        JpaSpecificationExecutor<AppPushConfig> {

}
