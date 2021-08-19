package com.wanmi.sbc.order.trade.repository;


import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 订单repository
 * Created by jinwei on 15/3/2017.
 */
@Repository
public interface TradeRepository extends MongoRepository<Trade, String> {

//    Trade findById(String id);

    List<Trade> findListByParentId(String parentId);

    Optional<Trade> findTopByIdAndTradeDelivers_Logistics_LogisticNo(String id, String logisticNo);

    Optional<Trade> findTopByTradeDelivers_Logistics_LogisticNoAndTradeDelivers_Logistics_logisticStandardCode
            (String id, String logisticNo);

    /**
     * 根据物流号，物流公司编码查询订单
     */
    @Query("{}")
    Optional<Trade> findAllByTradeDelivers_Logistics_LogisticNoAndTradeDelivers_Logistics_logisticStandardCode(List<Object> list);

    Page<Trade> findBySupplier_StoreIdAndTradeState_PayTimeBetweenAndTradeState_PayState(Long storeId, Date
            startTime, Date endTime, Pageable pageRequest, PayState payState);


    void deleteAllByYzTidIn(List<String> ids);

    Optional<Trade> findByYzTid(String yzTid);

}

