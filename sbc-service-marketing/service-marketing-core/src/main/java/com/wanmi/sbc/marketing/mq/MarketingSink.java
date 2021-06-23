package com.wanmi.sbc.marketing.mq;

import com.wanmi.sbc.customer.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.marketing.api.provider.constants.MarketingJmsDestinationConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/10 13:58
 * @version: 1.0
 */
public interface MarketingSink {

	/**
	 * 邀新注册-发放优惠券
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_MARKET_COUPON_INVITE_NEW_ADD)
	SubscribableChannel addCouponGroupFromInviteNew();

	/**
	 * 根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数）
	 * @return
	 */
	@Input(MarketingJmsDestinationConstants.Q_MARKET_GROUPON_MODIFY_STATISTICS_NUM)
	SubscribableChannel updateStatisticsNumByGrouponActivityId();

}
