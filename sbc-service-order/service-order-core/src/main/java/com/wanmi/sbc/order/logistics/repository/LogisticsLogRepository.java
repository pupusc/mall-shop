package com.wanmi.sbc.order.logistics.repository;


import com.wanmi.sbc.order.logistics.model.root.LogisticsLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 物流记录
 * Created by dyt on 2020/4/17.
 */
public interface LogisticsLogRepository extends MongoRepository<LogisticsLog, String> {


}

