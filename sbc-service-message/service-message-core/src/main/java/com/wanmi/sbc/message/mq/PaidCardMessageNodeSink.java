package com.wanmi.sbc.message.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 消息节点
 */
@EnableBinding
public interface PaidCardMessageNodeSink {

    String Q_SMS_SERVICE_MESSAGE_SEND = "q.sms.service.paidcard.message.send";

    @Input(Q_SMS_SERVICE_MESSAGE_SEND)
    SubscribableChannel receive();
}
