package com.wanmi.sbc.setting.wechatloginset.repository;

import com.wanmi.sbc.setting.wechatloginset.model.root.WechatLoginSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * <p>微信授权登录配置DAO</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@Repository
public interface WechatLoginSetRepository extends JpaRepository<WechatLoginSet, String>,
        JpaSpecificationExecutor<WechatLoginSet> {

    /**
     * 门店id查询微信授权配置
     * @param storeId
     * @return
     */
    Optional<WechatLoginSet> findByStoreId(Long storeId);

}
