package com.wanmi.sbc.marketing.marketingsuitssku.repository;

import com.wanmi.sbc.marketing.marketingsuitssku.model.root.MarketingSuitsSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQuery;
import java.util.List;
import java.util.Optional;

/**
 * <p>组合活动关联商品sku表DAO</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@Repository
public interface MarketingSuitsSkuRepository extends JpaRepository<MarketingSuitsSku, Long>,
        JpaSpecificationExecutor<MarketingSuitsSku> {

    int deleteByMarketingId(Long marketingId);

    /**
     * 根据活动id查询sku列表
     * @param marketingIds
     * @return
     */
    @Query("from MarketingSuitsSku where marketingId in (?1)")
    List<MarketingSuitsSku> getByMarketingIds(List<Long> marketingIds);
}
