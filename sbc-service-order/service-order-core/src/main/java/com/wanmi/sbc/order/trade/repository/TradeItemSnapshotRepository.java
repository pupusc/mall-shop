package com.wanmi.sbc.order.trade.repository;

import com.wanmi.sbc.order.trade.model.root.TradeItemSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <p>订单商品快照数据持久化操作</p>
 * Created by of628-wenzhi on 2017-07-13-上午10:50.
 */
@Repository
public interface TradeItemSnapshotRepository extends MongoRepository<TradeItemSnapshot, String> {

    Optional<TradeItemSnapshot> findByBuyerId(String buyerId);

    void deleteByBuyerId(String buyerId);
}
