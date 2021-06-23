package com.wanmi.sbc.marketing.discount.repository;

import com.wanmi.sbc.marketing.discount.model.entity.MarketingFullDiscountLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 满折关系
 */
@Repository
public interface MarketingFullDiscountLevelRepository extends JpaRepository<MarketingFullDiscountLevel, Long>, JpaSpecificationExecutor<MarketingFullDiscountLevel> {

    /**
     * 根据营销编号删除营销等级信息
     *
     * @param marketingId
     * @return
     */
    int deleteByMarketingId(Long marketingId);

    /**
     * 根据营销编号查询营销等级集合
     *
     * @param marketingId
     * @return
     */
    List<MarketingFullDiscountLevel> findByMarketingId(Long marketingId);

    /**
     * 根据营销id查询营销等级集合，按条件金额或数量从小到大排序
     *
     * @param marketingId
     * @return
     */
    List<MarketingFullDiscountLevel> findByMarketingIdOrderByFullAmountAscFullCountAsc(Long marketingId);

}
