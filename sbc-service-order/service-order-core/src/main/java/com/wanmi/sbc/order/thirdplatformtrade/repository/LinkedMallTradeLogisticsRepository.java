package com.wanmi.sbc.order.thirdplatformtrade.repository;


import com.wanmi.sbc.order.thirdplatformtrade.model.root.LinkedMallTradeLogistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * linkedmall订单物流repository
 * Created by yuhuiyu on 2020-8-22 13:06:07.
 */
@Repository
public interface LinkedMallTradeLogisticsRepository extends MongoRepository<LinkedMallTradeLogistics, String> {

    Optional<LinkedMallTradeLogistics> findTopByLmOrderIdAndMailNo(String lmOrderId, String mailNo);
}

