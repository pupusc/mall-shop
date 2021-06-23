package com.wanmi.sbc.elastic.mq.customerFunds;



import com.wanmi.sbc.elastic.api.constant.JmsDestinationConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author yangzhen
 * @Description //TODO
 * @Date 10:18 2020/12/16
 * @Param
 * @return
 **/
public interface EsCustomerFundsSink {

	/**
	 * 更新会员资金-会员账号字段
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_ACCOUNT)
	SubscribableChannel modifyEsCustomerAccountWithCustomerFunds();


	/**
	 * 更新会员资金-会员名称字段
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME)
	SubscribableChannel modifyEsCustomerNameWithCustomerFunds();



	/**
	 * 更新会员资金-会员名称、会员账号字段
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME_AND_ACCOUNT)
	SubscribableChannel modifyEsCustomerNameAndAccountWithCustomerFunds();

	/**
	 * 新增会员，初始化会员资金信息
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_ADD_INIT)
	SubscribableChannel initEsCustomerFunds();


	/**
	 * 邀新注册-发放邀新奖金
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT)
	SubscribableChannel grantEsAmountWithCustomerFunds();


	/**
	 * 更新es会员资金信息
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS)
	SubscribableChannel sendEsCustomerFunds();

	/**
	 * 批量更新es会员资金信息
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS_LIST)
	SubscribableChannel sendEsCustomerFundsList();

}
