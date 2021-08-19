package com.wanmi.sbc.marketing.gift.repository;

import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingFullGiftLevelRepository extends JpaRepository<MarketingFullGiftLevel, Long>, JpaSpecificationExecutor<MarketingFullGiftLevel> {

    /**
     * 根据营销编号删除营销等级信息
     *
     * @param marketingId
     * @return
     */
    int deleteByMarketingId(Long marketingId);

    /**
     * 根据营销id查询满赠等级集合，按条件金额或数量从小到大排序
     *
     * @param marketingId
     * @return
     */
    List<MarketingFullGiftLevel> findByMarketingIdOrderByFullAmountAscFullCountAsc(Long marketingId);
}