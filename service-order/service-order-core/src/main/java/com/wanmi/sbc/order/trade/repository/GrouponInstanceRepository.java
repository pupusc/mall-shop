package com.wanmi.sbc.order.trade.repository;


import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 团实例
 */
public interface GrouponInstanceRepository extends MongoRepository<GrouponInstance, String> {

    GrouponInstance findTopByGrouponNo(String grouponNo);

    GrouponInstance findTopByGrouponActivityIdAndGrouponStatusOrderByCreateTimeDesc(String grouponActivityId,GrouponOrderStatus grouponStatus);

}

