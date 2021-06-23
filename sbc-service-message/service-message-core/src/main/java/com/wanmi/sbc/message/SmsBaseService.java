package com.wanmi.sbc.message;

import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;

/**
 * @Author lvzhenwei
 * @Description 对接第三方短信平台调用接口
 * @Date 11:30 2019/12/4
 * @Param
 * @return
 **/
public interface SmsBaseService {

    SmsBaseResponse addSmsSign(SmsSign smsSign);

    SmsBaseResponse modifySmsSign(SmsSign smsSign);

    SmsBaseResponse deleteSmsSign(SmsSign smsSign);

    SmsBaseResponse querySmsSign(SmsSign smsSign);

    SmsBaseResponse addSmsTemplate(SmsTemplate smsTemplate);

    SmsBaseResponse modifySmsTemplate(SmsTemplate smsTemplate);

    SmsBaseResponse deleteSmsTemplate(SmsTemplate smsTemplate);

    SmsBaseResponse querySmsTemplate(SmsTemplate smsTemplate);

    SmsBaseResponse sendSms(SmsSendDetail smsSendDetail);

}
