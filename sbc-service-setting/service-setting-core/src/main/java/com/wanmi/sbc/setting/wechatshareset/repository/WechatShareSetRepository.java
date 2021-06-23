package com.wanmi.sbc.setting.wechatshareset.repository;

import com.wanmi.sbc.setting.wechatshareset.model.root.WechatShareSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * <p>微信分享配置DAO</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@Repository
public interface WechatShareSetRepository extends JpaRepository<WechatShareSet, String>,
        JpaSpecificationExecutor<WechatShareSet> {

    /**
     * 门店id查询微信分享配置
     * @param storeId
     * @return
     */
    Optional<WechatShareSet> findByStoreId(Long storeId);
}
