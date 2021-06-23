package com.wanmi.sbc.order.util;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.order.bean.enums.OrderSmsTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信服务
 * Created by aqlu on 15/12/4.
 */
@Component
public class SmsSendUtil {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 发送短信
     *
     * @param smsTemplate 短信模板
     * @param phones      手机号码
     * @param params      手机参数
     */
    public void send(OrderSmsTemplate smsTemplate, String[] phones, String... params) {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, String> dto = new HashMap<>();
        //取第二参数
        if ((OrderSmsTemplate.VIRTUAL_COUPON_CODE_PASSWORD.equals(smsTemplate)) && params.length > 1) {
            dto.put("code", params[0]);
            dto.put("password", params[1]);
        }else {
            dto.put("code", params[0]);
        }
        paramsMap.put("templateParamDTO", dto);
        paramsMap.put("businessType", smsTemplate.name());
        paramsMap.put("phoneNumbers", StringUtils.join(phones,","));
        resolver.resolveDestination(MQConstant.Q_SMS_SEND_CODE_MESSAGE_ADD).send(new GenericMessage<>(JSONObject.toJSONString(paramsMap)));

    }
}
