package com.wanmi.sbc.marketing.common.repository;

import com.wanmi.sbc.marketing.common.model.root.MarketingScope;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevelDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 营销和商品关联关系
 */
@Repository
public interface MarketingScopeRepository extends JpaRepository<MarketingScope, Long> {
    /**
     * 根据营销编号删除和商品的关联关系
     *
     * @param marketingId
     * @return
     */
    int deleteByMarketingId(Long marketingId);

    /**
     * 根据营销编号查询商品关联
     *
     * @param marketingId
     * @return
     */
    List<MarketingScope> findByMarketingId(Long marketingId);

    /**
     *  通过商品查询营销活动
     * @param scopeIds
     * @return
     */
    @Query("from MarketingScope where scopeId in (?1)")
    List<MarketingScope> getByGoodsInfoId(List<String> scopeIds);
}
