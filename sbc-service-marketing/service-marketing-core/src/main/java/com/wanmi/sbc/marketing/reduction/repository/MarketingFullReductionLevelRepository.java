package com.wanmi.sbc.marketing.reduction.repository;

import com.wanmi.sbc.marketing.reduction.model.entity.MarketingFullReductionLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingFullReductionLevelRepository extends JpaRepository<MarketingFullReductionLevel, Long>, JpaSpecificationExecutor<MarketingFullReductionLevel> {
    /**
     * 根据营销编号删除营销等级信息
     *
     * @param marketingId
     * @return
     */
    int deleteByMarketingId(Long marketingId);

    /**
     * 根据营销编号查询营销等级信息
     *
     * @param marketingId
     * @return
     */
    List<MarketingFullReductionLevel> findByMarketingId(Long marketingId);

    /**
     * 根据营销id查询营销等级集合，按条件金额或数量从小到大排序
     *
     * @param marketingId
     * @return
     */
    List<MarketingFullReductionLevel> findByMarketingIdOrderByFullAmountAscFullCountAsc(Long marketingId);
}
