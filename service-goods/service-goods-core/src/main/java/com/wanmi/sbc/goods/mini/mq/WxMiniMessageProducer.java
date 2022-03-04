package com.wanmi.sbc.goods.mini.mq;

import com.wanmi.sbc.goods.mini.mq.bean.WxLiveAssistantMessageData;
import com.wanmi.sbc.goods.mini.mq.config.WxLiveMessageSink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding
public class WxMiniMessageProducer {

    @Autowired
    private BinderAwareChannelResolver resolver;

    public void sendDelay(WxLiveAssistantMessageData msg, long delay){
        resolver.resolveDestination(WxLiveMessageSink.MSG_DELAY_LIVE_ASSISTANT_PRODUCER)
                .send(MessageBuilder.withPayload(msg).setHeader("x-delay", delay).build());
    }
}
