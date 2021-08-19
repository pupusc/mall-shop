package com.wanmi.sbc.marketing.marketingsuits.repository;

import com.wanmi.sbc.marketing.marketingsuits.model.root.MarketingSuits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>组合商品主表DAO</p>
 * @author zhk
 * @date 2020-04-02 10:39:15
 */
@Repository
public interface MarketingSuitsRepository extends JpaRepository<MarketingSuits, Long>,
        JpaSpecificationExecutor<MarketingSuits> {

    int deleteByMarketingId(Long marketingId);

    @Query("from MarketingSuits where marketingId in (?1)")
    List<MarketingSuits> getByMarketingIds(List<Long> marketingIds);

}