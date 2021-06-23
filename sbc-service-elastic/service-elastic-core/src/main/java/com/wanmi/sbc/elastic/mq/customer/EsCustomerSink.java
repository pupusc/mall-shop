package com.wanmi.sbc.elastic.mq.customer;


import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author dyt
 * @Description //TODO
 * @Date 10:18 2020/12/16
 * @Param
 * @return
 **/
public interface EsCustomerSink {

	/**
	 * 会员注册，同步ES
	 * @return
	 */
	@Input(MQConstant.Q_ES_SERVICE_CUSTOMER_REGISTER)
	SubscribableChannel register();

	/**
	 * 修改会员基本信息，同步ES
	 * @return
	 */
	@Input(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO)
	SubscribableChannel modifyBaseInfo();

	/**
	 * 修改会员账号，同步ES
	 * @return
	 */
	@Input(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_ACCOUNT)
	SubscribableChannel modifyCustomerAccount();

	/**
	 * 删除会员账号，同步ES
	 * @return
	 */
	@Input(MQConstant.Q_ES_SERVICE_CUSTOMER_DEL_CUSTOMER_INFO)
	SubscribableChannel delEsCustomerById();

	/**
	 * 修改会员是否分销员字段，同步ES
	 * @return
	 */
	@Input(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_DISTRIBUTOR)
	SubscribableChannel updateCustomerToDistributor();
}
