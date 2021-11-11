package com.wanmi.sbc.marketing.discount.repository;

import com.wanmi.sbc.marketing.discount.model.entity.MarketingFullDiscountLevel;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingPointBuyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingPointBuyLevelRepository extends JpaRepository<MarketingPointBuyLevel, Long>, JpaSpecificationExecutor<MarketingPointBuyLevel> {
}
