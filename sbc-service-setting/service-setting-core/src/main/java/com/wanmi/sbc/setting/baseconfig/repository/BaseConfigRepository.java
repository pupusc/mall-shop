package com.wanmi.sbc.setting.baseconfig.repository;

import com.wanmi.sbc.setting.baseconfig.model.root.BaseConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


/**
 * <p>基本设置DAO</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@Repository
public interface BaseConfigRepository extends JpaRepository<BaseConfig, Integer>,
        JpaSpecificationExecutor<BaseConfig> {

    @Query("select pcLogo from BaseConfig c")
    String queryBossLogo();
}
