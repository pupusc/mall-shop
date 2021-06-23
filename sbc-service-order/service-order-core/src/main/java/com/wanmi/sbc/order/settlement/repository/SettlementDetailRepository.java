package com.wanmi.sbc.order.settlement.repository;

import com.wanmi.sbc.order.settlement.model.root.SettlementDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 订单明细Repository
 * Created by hht on 2017/12/7.
 */
@Repository
public interface SettlementDetailRepository extends MongoRepository<SettlementDetail, String> {

    /**
     * 按照开始时间标志和结束时间标志删除明细
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param storeId 店铺Id
     * @return 影响行数
     */
    int deleteByStartTimeAndEndTimeAndStoreId(String startTime, String endTime, Long storeId);

    /**
     * 根据订单Id查询当前这个账期是否存在对应的结算明细
     *
     * @param tradeId 订单Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 结算明细对象
     */
    Optional<SettlementDetail> findBySettleTrade_TradeCodeAndStartTimeAndEndTime(String tradeId, String startTime,
                                                                                 String endTime);
}
