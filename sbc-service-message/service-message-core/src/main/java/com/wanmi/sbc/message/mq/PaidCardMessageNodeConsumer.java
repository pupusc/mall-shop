package com.wanmi.sbc.message.mq;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.MessageMQRequest;
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
@EnableBinding(PaidCardMessageNodeSink.class)
public class PaidCardMessageNodeConsumer {

    @Autowired
    private MessageSendHandler messageSendHandler;

    @Autowired
    private PushSendHandler pushSendHandler;


    @StreamListener(PaidCardMessageNodeSink.Q_SMS_SERVICE_MESSAGE_SEND)
    public void recevice(String json){
        log.info("接受到消息:{}",json);
        log.info("最新提交2");
        MessageMQRequest request = JSON.parseObject(json, MessageMQRequest.class);
        log.info("aaaaaa: {}",request);
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

    public static void main(String[] args) {
        String s = "{\"nodeCode\":\"PAID_CARD_WILL_VALID_REMAIN_CODE\",\"customerId\":\"2c90c72377e99a660177eb98600d0002\",\"params\":[\"黑卡会员\",\"2021\",\"3\",\"24\",\"https://mall-m-dpbaxtest.dushu365.com/\"],\"nodeType\":1,\"routeParam\":12}";
        MessageMQRequest request = JSON.parseObject(s, MessageMQRequest.class);
        System.out.println();

    }

}
