package com.wanmi.sbc.order.mq;

import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/10 15:11
 * @version: 1.0
 */
@EnableBinding
public interface OrderGrowthValueTempConsumptionSink {


	String OUTPUT = "growth-value-member-equity-order-output";
	String INPUT = "growth-value-member-equity-order-input";

	@Input(INPUT)
	SubscribableChannel receiveCoupon();

	@Output(OUTPUT)
	MessageChannel sendCoupon();
}
