package com.wanmi.sbc.mq;

import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/10 15:11
 * @version: 1.0
 */
public interface MobileSink {

	/**
	 * 团长开团-消息推送C端
	 * @return
	 */
	@Input(JmsDestinationConstants.Q_ORDER_SERVICE_OPEN_GROUPON_MESSAGE_PUSH_TO_C)
	SubscribableChannel openGrouponMsgToC();
}
