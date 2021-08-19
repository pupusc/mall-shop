package com.wanmi.sbc.setting.weibologinset.repository;

import com.wanmi.sbc.setting.weibologinset.model.root.WeiboLoginSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>微信登录配置DAO</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@Repository
public interface WeiboLoginSetRepository extends JpaRepository<WeiboLoginSet, String>,
        JpaSpecificationExecutor<WeiboLoginSet> {

}
