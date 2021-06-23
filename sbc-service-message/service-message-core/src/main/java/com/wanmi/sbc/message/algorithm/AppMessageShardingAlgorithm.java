package com.wanmi.sbc.message.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author
 * App站内信发送表-分表规则
 * PreciseShardingAlgorithm:精确分片算法，用于=、in场景
 */
@Slf4j
public class AppMessageShardingAlgorithm implements PreciseShardingAlgorithm<String> {

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> preciseShardingValue) {
		log.info("=======App站内信消息分表开始=========");
		String logicTableName= preciseShardingValue.getLogicTableName();
		int value = Math.abs(preciseShardingValue.getValue().hashCode() % availableTargetNames.size());
		log.info("=======App站内信消息分表结束，最终路由表名：{}=========",logicTableName  + "_" + value);
		return logicTableName  + "_" + value;
	}
}
