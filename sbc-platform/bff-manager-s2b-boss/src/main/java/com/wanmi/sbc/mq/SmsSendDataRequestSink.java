package com.wanmi.sbc.mq;

import com.wanmi.sbc.message.api.constant.SendDataConstants;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author: zgl
 * @Date:
 * @Description: 短信发送消息队列 Sink
 */
@EnableBinding
public interface SmsSendDataRequestSink {

    @Input(SendDataConstants.Q_SMS_SEND_INPUT)
    SubscribableChannel receive();
}
