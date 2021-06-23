package com.wanmi.sbc.order.yzorder.repository;

import com.wanmi.sbc.order.yzorder.model.root.YzOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YzOrderRepository extends MongoRepository<YzOrder,String> {
}
