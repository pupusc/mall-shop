package com.wanmi.sbc.goods.mini.mq.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface WxLiveMessageSink {

    String MSG_DELAY_LIVE_ASSISTANT_PRODUCER = "msg-live-assistant-producer";

    String MSG_DELAY_LIVE_ASSISTANT_CONSUMER = "msg-live-assistant-consumer";

    @Input(WxLiveMessageSink.MSG_DELAY_LIVE_ASSISTANT_CONSUMER)
    SubscribableChannel wxLiveDelayMessageConsume();
}
