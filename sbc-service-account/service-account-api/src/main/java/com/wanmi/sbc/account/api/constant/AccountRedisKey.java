package com.wanmi.sbc.account.api.constant;

/**
 * Redis key
 * @author: Geek Wang
 * @createDate: 2019/4/16 10:29
 * @version: 1.0
 */
public interface AccountRedisKey {

	/**
	 * 余额总额
	 */
	String ACCOUNT_BALANCE_TOTAL = "account:funds:accountBalanceTotal";

	/**
	 * 冻结余额总额
	 */
	String BLOCKED_BALANCE_TOTAL = "account:funds:blockedBalanceTotal";

	/**
	 * 可提现余额总额
	 */
	String WITHDRAW_AMOUNT_TOTAL = "account:funds:withdrawAmountTotal";
}
