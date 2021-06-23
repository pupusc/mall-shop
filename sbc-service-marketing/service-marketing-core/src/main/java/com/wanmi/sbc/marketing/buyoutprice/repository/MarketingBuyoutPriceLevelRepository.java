package com.wanmi.sbc.marketing.buyoutprice.repository;

import com.wanmi.sbc.marketing.buyoutprice.model.entry.MarketingBuyoutPriceLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingBuyoutPriceLevelRepository extends JpaRepository<MarketingBuyoutPriceLevel, Long>,
        JpaSpecificationExecutor<MarketingBuyoutPriceLevel> {

    /**
     * 根据营销编号删除营销等级信息
     *
     * @param marketingId
     * @return
     */
    int deleteByMarketingId(Long marketingId);

    /**
     * 查询一口价营销规则列表
     * @param marketingId
     * @return
     */
    List<MarketingBuyoutPriceLevel> findByMarketingId(Long marketingId);

    /**
     * 根据营销id查询营销等级集合，按条件金额从小到大排序
     *
     * @param marketingId
     * @return
     */
    List<MarketingBuyoutPriceLevel> findByMarketingIdOrderByFullAmountAsc(Long marketingId);


    /**
     * 查询一口价营销规则列表
     * @param marketingIds
     * @return
     */
    List<MarketingBuyoutPriceLevel> findByMarketingIdIn(List<Long> marketingIds);

}
