package com.wanmi.sbc.marketing.points.repository;

import com.wanmi.sbc.marketing.marketingsuits.model.root.MarketingSuits;
import com.wanmi.sbc.marketing.points.model.root.PointsExchangeActivity;
import com.wanmi.sbc.marketing.points.model.root.PointsExchangeActivityGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PointsExchangeActivityGoodsRepository extends JpaRepository<PointsExchangeActivityGoods, Integer>,
        JpaSpecificationExecutor<PointsExchangeActivityGoods> {

    @Query("from PointsExchangeActivityGoods where activityId = ?1")
    List<PointsExchangeActivityGoods> getByActId(Integer activityId);
}