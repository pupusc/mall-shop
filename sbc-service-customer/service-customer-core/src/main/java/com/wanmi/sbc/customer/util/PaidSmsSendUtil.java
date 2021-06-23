package com.wanmi.sbc.customer.util;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.customer.bean.enums.PaidCardSmsTemplate;
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
public class PaidSmsSendUtil {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 发送短信
     *
     * @param smsTemplate 短信模板
     * @param phones      手机号码
     * @param params      手机参数
     */
    public void send(PaidCardSmsTemplate smsTemplate, String[] phones, String... params) {

        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, String> dto = new HashMap<>();
        //取第二参数
        if ((PaidCardSmsTemplate.PAID_CARD_BUY_CODE.equals(smsTemplate)) && params.length > 1) {
            dto.put("paidCardName",params[0]);
            dto.put("year",params[1]);
            dto.put("month",params[2]);
            dto.put("day",params[3]);
        }else if(PaidCardSmsTemplate.PAID_CARD_WILL_VALID_REMAIN_CODE.equals(smsTemplate)) {
            dto.put("paidCardName",params[0]);
            dto.put("year",params[1]);
            dto.put("month",params[2]);
            dto.put("day",params[3]);
        }else if(PaidCardSmsTemplate.PAID_CARD_VALID_REMAIN_CODE.equals(smsTemplate)){
            dto.put("paidCardName",params[0]);
            dto.put("cardName",params[0]);
        }

        paramsMap.put("templateParamDTO", dto);
        paramsMap.put("businessType", smsTemplate.name());
        paramsMap.put("phoneNumbers", StringUtils.join(phones,","));
        resolver.resolveDestination(MQConstant.Q_SMS_SEND_CODE_MESSAGE_ADD)
                .send(new GenericMessage<>(JSONObject.toJSONString(paramsMap)));

    }
}
