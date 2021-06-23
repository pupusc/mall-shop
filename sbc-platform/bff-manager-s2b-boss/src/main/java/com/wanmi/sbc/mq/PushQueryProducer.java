package com.wanmi.sbc.mq;

import com.wanmi.sbc.message.api.constant.PushConstants;
import com.wanmi.sbc.message.bean.vo.PushSendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @program: sbc-micro-service
 * @create: 2020-02-04 17:38
 **/
@Service
@EnableBinding
public class PushQueryProducer {
    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 运营计划push查询详情
     * @param pushSendVO
     */
    public void query(PushSendVO pushSendVO){
        resolver.resolveDestination(PushConstants.Q_SMS_SERVICE_PUSH_QUERY_OUTPUT).send
                (MessageBuilder.withPayload(pushSendVO).setHeader("x-delay", 600000).build());
    }
}