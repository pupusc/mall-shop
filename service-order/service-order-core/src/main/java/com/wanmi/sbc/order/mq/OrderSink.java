package com.wanmi.sbc.order.mq;

import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/10 15:11
 * @version: 1.0
 */
public interface OrderSink {

	/**
	 * 团长开团MQ处理
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_OPEN_GROUPON_CONSUMER)
	SubscribableChannel openGroupon();

	/**
	 * 超过一定时间未支付，自动取消订单
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_CANCEL_ORDER_CONSUMER)
	SubscribableChannel cancelOrder();

	/**
	 * 拼团订单-支付成功，订单异常，自动退款
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_GROUPON_PAY_SUCCESS_AUTO_REFUND)
	SubscribableChannel handleGrouponOrderRefund();

	/**
	 * 拼团订单-支付成功，订单异常，自动退款
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_GROUPON_JOIN_NUM_LIMIT_CONSUMER)
	SubscribableChannel handleGrouponNumLimit();

	/**
	 * 业务员交接数据
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_MODIFY_EMPLOYEE_DATA)
	SubscribableChannel modifyEmployeeData();


	/**
	 * 返还限售数量
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_REDUCE_RESTRICTED_PURCHASE_NUM)
	SubscribableChannel backRestrictedPurchaseNum();


	/**
	 * 修改或保存积分订单抵扣异常信息
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_MODIFY_OR_ADD_TRADE_POINTS_EXCEPTION)
	SubscribableChannel addTradePointsException();

	/**
	 * 新增会员权益处理订单成长值 临时表
	 * @return
	 */
	@Input(JmsDestinationConstants.GROWTH_VALUE_MEMBER_EQUITY_ORDER)
	SubscribableChannel orderGrowthValueTemp();

	/**
	 * 订单发货状态变更
	 * */
	@Input(JmsDestinationConstants.Q_OPEN_ORDER_DELIVERED_CONSUMER)
	SubscribableChannel openOrderDelivered();
}
