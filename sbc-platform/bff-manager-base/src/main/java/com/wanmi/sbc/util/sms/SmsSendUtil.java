package com.wanmi.sbc.util.sms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.message.api.provider.smsbase.SmsBaseProvider;
import com.wanmi.sbc.message.api.request.smsbase.SmsSendRequest;
import com.wanmi.sbc.message.bean.dto.SmsTemplateParamDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短信服务
 * Created by aqlu on 15/12/4.
 */
@Component
public class SmsSendUtil {

    @Autowired
    private SmsBaseProvider smsBaseProvider;

    /**
     * 发送短信
     *
     * @param smsTemplate 短信模板
     * @param phones      手机号码
     * @param params      手机参数
     */
    public boolean send(SmsTemplate smsTemplate, String[] phones, String... params) {
        SmsTemplateParamDTO dto = new SmsTemplateParamDTO();
        //取第二参数
        if ((SmsTemplate.CUSTOMER_PASSWORD.equals(smsTemplate)
                || SmsTemplate.EMPLOYEE_PASSWORD.equals(smsTemplate)) && params.length > 1) {
            dto.setAccount(params[0]);
            dto.setPassword(params[1]);
        }else {
            dto.setCode(params[0]);
        }
        BaseResponse response = smsBaseProvider.send(SmsSendRequest.builder()
                .businessType(smsTemplate.name())
                .phoneNumbers(StringUtils.join(phones,","))
                .templateParamDTO(dto)
                .build());
        return CommonErrorCode.SUCCESSFUL.equals(response.getCode());
    }
}
