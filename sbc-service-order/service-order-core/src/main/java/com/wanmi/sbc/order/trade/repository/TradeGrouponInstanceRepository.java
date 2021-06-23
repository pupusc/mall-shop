package com.wanmi.sbc.order.trade.repository;

import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:26 2018/9/30
 * @Description: 订单组repository
 */
public interface TradeGrouponInstanceRepository extends MongoRepository<GrouponInstance, String> {

}
