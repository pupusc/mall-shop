package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.constant.SendDataConstants;
import com.wanmi.sbc.message.api.provider.smssend.SmsSendSaveProvider;
import com.wanmi.sbc.message.api.request.smssend.SmsSendSaveRequest;
import com.wanmi.sbc.message.bean.dto.SmsSendDTO;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 发送任务消费
 * Author: zgl
 * Time: 2019/12/11.10:54
 */
@Slf4j
@Component
@EnableBinding(SmsSendDataRequestSink.class)
public class SmsSendDataRequestConsumer {
    @Autowired
    private SmsSendTaskService smsSendTaskService;

    @Autowired
    private SmsSendSaveProvider smsSendSaveProvider;

    /**
     *
     */
    @Transactional
    @StreamListener(SendDataConstants.Q_SMS_SEND_INPUT)
    public void receive(String json,
                        @Header(AmqpHeaders.CHANNEL) Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws Exception  {
        System.out.println("消费任务内容：".concat(json));
        SmsSendDTO smsSend = JSONObject.parseObject(json, SmsSendDTO.class);
        try {
            this.smsSendTaskService.send(smsSend);
        }catch (Exception e){
            log.error("发送任务失败：{}",e);
            smsSend.setStatus(SendStatus.FAILED);
            smsSend.setMessage("执行发送任务异常！");
            smsSendSaveProvider.save(KsBeanUtil.convert(smsSend,SmsSendSaveRequest.class));
        }finally {
            channel.basicAck(deliveryTag,true);
        }

    }


}
