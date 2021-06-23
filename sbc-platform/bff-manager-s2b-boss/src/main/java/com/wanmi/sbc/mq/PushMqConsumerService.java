package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.message.api.constant.PushConstants;
import com.wanmi.sbc.message.api.request.pushsend.PushSendAddRequest;
import com.wanmi.sbc.pushsend.service.PushSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @program: sbc-micro-service
 * @description: crm发送push mq消费端
 * @create: 2020-01-14 19:50
 **/
@Service
@Slf4j
@EnableBinding(PushSink.class)
public class PushMqConsumerService {
    @Autowired
    private PushSendService pushSendService;

    @StreamListener(PushConstants.Q_SMS_SERVICE_PUSH_ADD)
    public void pushAdd(String msg){
        log.info("=============== crm推送pushMQ处理start ===============");
        PushSendAddRequest request = JSONObject.parseObject(msg, PushSendAddRequest.class);
        pushSendService.add(request);
        log.info("=============== crm推送pushMQ处理end ===============");
    }
}