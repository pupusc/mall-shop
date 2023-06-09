package com.wanmi.sbc.marketing.discount.repository;

import com.wanmi.sbc.marketing.discount.model.entity.MarketingPointBuyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingPointBuyLevelRepository extends JpaRepository<MarketingPointBuyLevel, Long>, JpaSpecificationExecutor<MarketingPointBuyLevel> {

    List<MarketingPointBuyLevel> findByMarketingIdOrderByMoneyAsc(Long marketingId);

    List<MarketingPointBuyLevel> findAllByMarketingIdIn(List<Long> marketingIds);

    List<MarketingPointBuyLevel> findAllByMarketingIdInAndSkuIdIn(List<Long> marketingIds, List<String> skuIds);

    void deleteByMarketingId(Long marketingId);


}
