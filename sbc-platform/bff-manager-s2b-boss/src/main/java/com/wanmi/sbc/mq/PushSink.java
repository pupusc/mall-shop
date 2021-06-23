package com.wanmi.sbc.mq;

import com.wanmi.sbc.message.api.constant.PushConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @program: sbc-micro-service
 * @description: Crm推送push
 * @create: 2020-01-14 19:52
 **/
public interface PushSink {
    /**
     * Crm推送push
     * @return
     */
    @Input(PushConstants.Q_SMS_SERVICE_PUSH_ADD)
    SubscribableChannel pushAdd();
}
