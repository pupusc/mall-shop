package com.wanmi.perseus.mq.sensors.constant;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 神策埋点消息
 */
public class SensorsMessageConstants {

    /**
     * 神策埋点消息消费
     */
    public static final String Q_SENSORS_MESSAGE_CONSUMER = "sensors-message-consumer";

}
