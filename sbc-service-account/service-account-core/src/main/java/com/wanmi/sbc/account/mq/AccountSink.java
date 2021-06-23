package com.wanmi.sbc.account.mq;

import com.wanmi.sbc.customer.api.constant.JmsDestinationConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/10 13:58
 * @version: 1.0
 */
public interface AccountSink {

	/**
	 * 更新会员资金-会员账号字段
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_ACCOUNT)
	SubscribableChannel modifyCustomerAccountWithCustomerFunds();

	/**
	 * 更新会员提现-会员账号字段
	 * @return
	 */
	@Input( JmsDestinationConstants.Q_ACCOUNT_DRAW_CASH_MODIFY_CUSTOMER_ACCOUNT)
	SubscribableChannel modifyCustomerAccountWithCustomerDrawCash();

	/**
	 * 更新会员资金-会员名称字段
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME)
	SubscribableChannel modifyCustomerNameWithCustomerFunds();

	/**
	 * 更新会员提现-会员名称字段
	 * @author chenyufei
	 */
	@Input(JmsDestinationConstants.Q_ACCOUNT_DRAW_CASH_MODIFY_CUSTOMER_NAME)
	SubscribableChannel modifyCustomerNameWithCustomerDrawCash();

	/**
	 * 更新会员资金-会员名称、会员账号字段
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME_AND_ACCOUNT)
	SubscribableChannel modifyCustomerNameAndAccountWithCustomerFunds();

	/**
	 * 新增会员，初始化会员资金信息
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ACCOUNT_FUNDS_ADD_INIT)
	SubscribableChannel initCustomerFunds();

	/**
	 * 新增分销员，更新会员资金-是否分销员字段
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_IS_DISTRIBUTOR)
	SubscribableChannel modifyIsDistributorWithCustomerFunds();

	/**
	 * 邀新注册-发放邀新奖金
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT)
	SubscribableChannel grantAmountWithCustomerFunds();

}
