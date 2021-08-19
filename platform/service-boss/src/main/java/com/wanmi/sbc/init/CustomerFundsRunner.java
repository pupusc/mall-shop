package com.wanmi.sbc.init;

import com.wanmi.sbc.account.api.constant.AccountRedisKey;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsProvider;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 会员资金初始化Redis
 * @author: Geek Wang
 * @createDate: 2019/4/25 10:00
 * @version: 1.0
 */
@Slf4j
@Order(1)
@Component
public class CustomerFundsRunner implements CommandLineRunner {

	@Autowired
	private RedisService redisService;

	@Autowired
	private CustomerFundsProvider customerFundsProvider;

	@Override
	public void run(String... args) throws Exception {
		String zero = "0.00";
		String accountBalanceTotal = Objects.toString(redisService.getValueByKey(AccountRedisKey.ACCOUNT_BALANCE_TOTAL),zero);
		String blockedBalanceTotal = Objects.toString(redisService.getValueByKey(AccountRedisKey.BLOCKED_BALANCE_TOTAL),zero);
		String withdrawAmountTotal = Objects.toString(redisService.getValueByKey(AccountRedisKey.WITHDRAW_AMOUNT_TOTAL),zero);

		if (!zero.equals(accountBalanceTotal) && !zero.equals(blockedBalanceTotal) && !zero.equals(withdrawAmountTotal)){
			log.info("==============会员资金初始化Redis，统计数据存在，无需初始化====================");
			return;
		}
		BaseResponse baseResponse = customerFundsProvider.initStatisticsCache();
		log.info("========会员资金初始化Redis，是否成功：{}==============",baseResponse.getContext().equals(Boolean.TRUE) ? "成功" : "失败");
	}
}
