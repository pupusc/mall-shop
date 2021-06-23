package com.wanmi.sbc.message.mq;

import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 消息节点
 */
@EnableBinding
public interface SmsSendSink {

    String Q_SMS_SEND_MESSAGE_ADD = MQConstant.Q_SMS_SEND_MESSAGE_ADD;

    String Q_SMS_SEND_CODE_MESSAGE_ADD = MQConstant.Q_SMS_SEND_CODE_MESSAGE_ADD;

    @Input(MQConstant.Q_SMS_SEND_MESSAGE_ADD)
    SubscribableChannel receive();

    @Input(MQConstant.Q_SMS_SEND_CODE_MESSAGE_ADD)
    SubscribableChannel receiveCode();

}
