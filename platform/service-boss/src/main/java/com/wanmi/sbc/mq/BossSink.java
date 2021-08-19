package com.wanmi.sbc.mq;

import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/10 15:11
 * @version: 1.0
 */
public interface BossSink {

	/**
	 * 订单支付MQ处理
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_PAYED)
	SubscribableChannel doOrderPayed();

	/**
	 * 订单完成MQ处理
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_COMPLETE)
	SubscribableChannel doOrderComplete();

	/**
	 * 积分订单完成MQ处理
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_POINTS_ORDER_COMPLETE)
	SubscribableChannel doPointsOrderComplete();


	/**
	 * 订单支付&订单完成MQ处理
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_PAYED_AND_COMPLETE)
	SubscribableChannel doOrderPayedAndComplete();

	/**
	 * 订单退款作废MQ处理
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_ORDER_REFUND_VOID)
	SubscribableChannel doOrderRefundVoid();

	/**
	 * 退单状态变更MQ处理：分销任务临时表退单数量加减
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_RETURN_ORDER_FLOW)
	SubscribableChannel doReturnOrderInit();

	/**
	 * 订单-支付成功，订单与第三方平台订单同步
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_THIRD_PLATFORM_SYNC)
	SubscribableChannel doThirdPlatformSync();
}
