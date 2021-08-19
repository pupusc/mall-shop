package com.wanmi.sbc.marketing.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author
 * 优惠券券码-分表规则
 * PreciseShardingAlgorithm:精确分片算法，用于=、in场景
 */
@Slf4j
public class CouponCodeShardingAlgorithm implements PreciseShardingAlgorithm<String> {

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> preciseShardingValue) {
		log.info("=======优惠券券码分表开始=========");
		String logicTableName= preciseShardingValue.getLogicTableName();
		int value = Math.abs(preciseShardingValue.getValue().hashCode() % availableTargetNames.size());
		log.info("=======优惠券券码分表结束，最终路由表名：{}=========",logicTableName  + "_" + value);
		return logicTableName  + "_" + value;
	}
}
