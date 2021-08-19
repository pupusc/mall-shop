package com.wanmi.sbc.elastic.mq.coupon;


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
public interface EsCouponActivitySink {

	/**
	 * 新增积分兑换券，同步ES
	 * @return
	 */
	@Input(MQConstant.Q_ES_SERVICE_COUPON_ADD_POINTS_COUPON)
	SubscribableChannel addPointsCoupon();
}
