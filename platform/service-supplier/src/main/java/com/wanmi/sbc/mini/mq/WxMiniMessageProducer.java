//package com.wanmi.sbc.mini.mq;
//
//import com.wanmi.sbc.mini.mq.config.WxLiveMessageSink;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
//import org.springframework.integration.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//@EnableBinding
//public class WxMiniMessageProducer {
//
//    @Autowired
//    private BinderAwareChannelResolver resolver;
//
//    public void sendDelay(Map<String, Object> map, long delay){
//        if(delay <= 0){
//            delay = 1500;
//        }
//        resolver.resolveDestination(WxLiveMessageSink.MSG_DELAY_LIVE_ASSISTANT_PRODUCER)
//                .send(MessageBuilder.withPayload(map).setHeader("x-delay", delay).build());
//    }
//}
