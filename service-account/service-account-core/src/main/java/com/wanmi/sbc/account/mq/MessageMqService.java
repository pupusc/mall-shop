package com.wanmi.sbc.account.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.customer.api.constant.JmsDestinationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableBinding
public class MessageMqService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 发送push、站内信、短信
     * @param request
     */
    public void sendMessage(MessageMQRequest request){
        resolver.resolveDestination(JmsDestinationConstants.Q_SMS_SERVICE_MESSAGE_SEND).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }
}
