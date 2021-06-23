package com.wanmi.sbc.message.mq;

import com.wanmi.sbc.message.api.constant.PushConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-02-04 17:27
 **/
public interface PushQuerySink {

    @Input(PushConstants.Q_SMS_SERVICE_PUSH_QUERY_INPUT)
    SubscribableChannel pushQuery();
}
