package com.wanmi.sbc.message.mq;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.message.handle.MessageSendHandler;
import com.wanmi.sbc.message.handle.PushSendHandler;
import com.wanmi.sbc.message.handle.SmsNoticeSendHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * 发送消息
 */
@Slf4j
@Component
@EnableBinding(MessageNodeSink.class)
public class MessageNodeConsumer {

    @Autowired
    private MessageSendHandler messageSendHandler;

    @Autowired
    private PushSendHandler pushSendHandler;

    @Autowired
    private SmsNoticeSendHandler smsNoticeSendHandler;

    @StreamListener(MessageNodeSink.Q_SMS_SERVICE_MESSAGE_SEND)
    public void recevice(String json){
        MessageMQRequest request = JSON.parseObject(json, MessageMQRequest.class);
        try {
            smsNoticeSendHandler.handle(request);
        }catch (Exception e){
            log.error("短信通知消费异常", e);
        }

        try {
            messageSendHandler.handle(request);
        }catch (Exception e){
            log.error("站内信通知消费异常", e);
        }

        try {
            pushSendHandler.handle(request);
        }catch (Exception e){
            log.error("Push节点通知消费异常", e);
        }
    }

}
