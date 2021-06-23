package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.order.api.constant.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

/**
 * 通知节点消息发送生产者
 */
@Service
@EnableBinding
public class MessageSendProducer {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 发送push、站内信、短信
     * @param request
     */
    public void sendMessage(MessageMQRequest request){
        resolver.resolveDestination(MessageConstants.Q_SMS_SERVICE_MESSAGE_SEND).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }
}
