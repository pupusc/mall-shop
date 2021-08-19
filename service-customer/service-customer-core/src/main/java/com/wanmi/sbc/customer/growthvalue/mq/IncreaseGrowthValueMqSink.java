package com.wanmi.sbc.customer.growthvalue.mq;

import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * mq接收增长成长值的信息 Sink
 */
public interface IncreaseGrowthValueMqSink {

    @Input(MQConstant.INCREASE_GROWTH_VALUE)
    SubscribableChannel increaseGrowthValueMq();

}
