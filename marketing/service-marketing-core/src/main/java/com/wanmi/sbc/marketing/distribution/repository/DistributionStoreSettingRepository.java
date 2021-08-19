package com.wanmi.sbc.marketing.distribution.repository;

import com.wanmi.sbc.marketing.distribution.model.DistributionStoreSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>店铺分销设置DAO</p>
 * @author gaomuwei
 * @date 2019-02-19 15:46:27
 */
@Repository
public interface DistributionStoreSettingRepository extends JpaRepository<DistributionStoreSetting, String> {

    /**
     * 查询店铺分销设置
     *
     * @param storeId 店铺id
     * @return
     */
    DistributionStoreSetting findByStoreId(String storeId);

    /**
     * 根据店铺ID集合查询分销设置
     * @param storeIds
     * @return
     */
    List<DistributionStoreSetting> findByStoreIdIn(List<String> storeIds);

}
