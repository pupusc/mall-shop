package com.wanmi.perseus.mq.sensors.processor;

import com.wanmi.perseus.mq.sensors.constant.SensorsMessageConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SensorsMessageSink {

    @Input(SensorsMessageConstants.Q_SENSORS_MESSAGE_CONSUMER)
    SubscribableChannel sensorsConsume();

}
