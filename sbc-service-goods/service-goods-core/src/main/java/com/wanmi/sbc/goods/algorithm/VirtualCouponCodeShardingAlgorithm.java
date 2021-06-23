package com.wanmi.sbc.goods.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author 卡券券码-分表规则
 * PreciseShardingAlgorithm:精确分片算法，用于=、in场景
 */
@Slf4j
public class VirtualCouponCodeShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        log.info("=======卡券券码分表开始=========");
        String logicTableName = preciseShardingValue.getLogicTableName();
        long value = Long.parseLong(preciseShardingValue.getValue() + "") % collection.size();
        log.info("=======卡券券码分表结束，最终路由表名：{}=========", logicTableName + "_" + value);
        return logicTableName + "_" + value;
    }
}
