package com.wanmi.sbc.setting.onlineservice.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.onlineservice.model.root.OnlineService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>onlineServiceDAO</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@Repository
public interface OnlineServiceRepository extends JpaRepository<OnlineService, Integer>,
        JpaSpecificationExecutor<OnlineService> {

    /**
     * 通过店铺id appKey 查询在线客服
     * @param storeId
     * @param deleteFlag
     * @return
     */
    OnlineService findByStoreIdAndDelFlag(Long storeId, DeleteFlag deleteFlag);

}
