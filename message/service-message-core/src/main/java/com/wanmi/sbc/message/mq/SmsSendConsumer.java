package com.wanmi.sbc.message.mq;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.SmsProxy;
import com.wanmi.sbc.message.api.request.smsbase.SmsSendRequest;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 发送消息
 */
@Slf4j
@Component
@EnableBinding(SmsSendSink.class)
public class SmsSendConsumer {

    @Autowired
    private SmsProxy smsProxy;

    @StreamListener(SmsSendSink.Q_SMS_SEND_MESSAGE_ADD)
    public void recevice(String json){
        SmsSendDetail smsSendDetail = JSON.parseObject(json, SmsSendDetail.class);
        smsProxy.sendSms(smsSendDetail);
    }


    @StreamListener(SmsSendSink.Q_SMS_SEND_CODE_MESSAGE_ADD)
    public void receiveCode(String json){
        SmsSendRequest request = JSON.parseObject(json, SmsSendRequest.class);
        SmsSendDetail smsSend = new SmsSendDetail();
        KsBeanUtil.copyPropertiesThird(request, smsSend);
        if(Objects.nonNull(request.getTemplateParamDTO())) {
            smsSend.setTemplateParam(JSON.toJSONString(request.getTemplateParamDTO()));
        }
        smsProxy.sendSms(smsSend);
    }
}
