package com.wanmi.sbc.init;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.constant.DistributionCommissionRedisKey;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCommissionQueryProvider;
import com.wanmi.sbc.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 分销佣金初始化Redis
 * @author: Geek Wang
 * @createDate: 2019/4/25 10:08
 * @version: 1.0
 */
@Slf4j
@Order(2)
@Component
public class DistributionCommissionRunner implements CommandLineRunner {

	@Autowired
	private RedisService redisService;

	@Autowired
	private DistributionCommissionQueryProvider distributionCommissionQueryProvider;

	@Override
	public void run(String... args) throws Exception {
		String zero = "0.00";
		//佣金总额
		String commissionTotal = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL),zero);
		//分销佣金
		String commission = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION),zero);
		//邀新奖金
		String rewardCash = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH),zero);
		//未入账分销佣金
		String commissionNotRecorded = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_NOTRECORDED),zero);
		//未入账邀新奖金
		String rewardCashNotRecorded = Objects.toString(redisService.getValueByKey(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED),zero);

		if (!zero.equals(commissionTotal) && !zero.equals(commission) && !zero.equals(rewardCash) && !zero.equals(commissionNotRecorded) && !zero.equals(rewardCashNotRecorded)){
			log.info("==============分销佣金初始化Redis，统计数据存在，无需初始化====================");
			return;
		}
		BaseResponse baseResponse = distributionCommissionQueryProvider.initStatisticsCache();
		log.info("========分销佣金初始化Redis,是否成功：{}==============",baseResponse.getContext().equals(Boolean.TRUE) ? "成功" : "失败");
	}
}
