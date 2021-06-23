package com.wanmi.sbc.marketing.halfpricesecondpiece.repository;

import com.wanmi.sbc.marketing.halfpricesecondpiece.model.entry.MarketingHalfPriceSecondPieceLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HalfPriceSecondPieceLevelRepository extends JpaRepository<MarketingHalfPriceSecondPieceLevel, Long>,
        JpaSpecificationExecutor<MarketingHalfPriceSecondPieceLevel> {

    /**
     * 根据营销编号删除营销等级信息
     *
     * @param marketingId
     * @return
     */
    int deleteByMarketingId(Long marketingId);

    /**
     * 查询第二件半价规则列表
     * @param marketingId
     * @return
     */
    MarketingHalfPriceSecondPieceLevel findByMarketingId(Long marketingId);

    /**
     * 根据营销id查询营销等级集合，按条件件数从小到大排序
     *
     * @param marketingId
     * @return
     */
    List<MarketingHalfPriceSecondPieceLevel> findByMarketingIdOrderByNumberAsc(Long marketingId);

    /**
     * 查询第二件半价规则列表
     * @param marketingIds
     * @return
     */
    List<MarketingHalfPriceSecondPieceLevel> findByMarketingIdIn(List<Long> marketingIds);

}
