package com.wanmi.sbc.marketing.gift.repository;

import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingFullGiftDetailRepository extends JpaRepository<MarketingFullGiftDetail, Long>, JpaSpecificationExecutor<MarketingFullGiftDetail> {
    /**
     * 根据营销编号删除营销赠品信息
     *
     * @param marketingId
     * @return
     */
    int deleteByMarketingId(Long marketingId);

    /**
     * 根据营销Id查询满赠详情集合
     *
     * @param marketingId
     * @return
     */
    List<MarketingFullGiftDetail> findByMarketingId(Long marketingId);

    /**
     * 根据营销Id查询满赠详情集合
     *
     * @param marketingId
     * @return
     */
    List<MarketingFullGiftDetail> findByMarketingIdAndGiftLevelId(Long marketingId, Long giftLevelId);
}