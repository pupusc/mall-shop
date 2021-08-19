package com.wanmi.sbc.order.thirdplatformtrade.repository;


import com.wanmi.sbc.order.thirdplatformtrade.model.root.ThirdPlatformTrade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 第三方渠道订单repository
 * Created by jinwei on 15/3/2017.
 */
@Repository
public interface ThirdPlatformTradeRepository extends MongoRepository<ThirdPlatformTrade, String> {


    List<ThirdPlatformTrade> findListByParentId(String parentId);

    List<ThirdPlatformTrade> findListByParentIdIn(List<String> parentIds);

    List<ThirdPlatformTrade> findListByTradeIdIn(List<String> tradeId);

    List<ThirdPlatformTrade> findListByTradeId(String tradeId);

    ThirdPlatformTrade findFirstById(String id);

}

