package com.wanmi.sbc.setting.tradeOrder.repository;

import com.wanmi.sbc.setting.tradeOrder.model.root.GoodsMonth;
import com.wanmi.sbc.setting.tradeOrder.model.root.TradeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsMonthRepository extends JpaRepository<GoodsMonth, Integer>,
        JpaSpecificationExecutor<GoodsMonth> {
}
