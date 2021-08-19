package com.wanmi.sbc.message;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.message.api.constant.SmsErrorCode;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingQueryRequest;
import com.wanmi.sbc.message.configuration.ApplicationConfig;
import com.wanmi.sbc.message.constant.SmsBusinessConstant;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import com.wanmi.sbc.message.smssetting.service.SmsSettingService;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-4
 * \* Time: 14:29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Component
public class SmsProxy  {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String BEAN_NAME = "smsBusinessBean";

    @Autowired
    private SmsSettingService smsSettingService;

    public SmsBaseService getBean(){
        /*
        //如果发送频繁或者性能上不去可以先将bean注册到beanFactory,修改时更新
        if(ApplicationConfig.CONTEXT.containsBeanDefinition(BEAN_NAME)){
            return (SmsBaseService) ApplicationConfig.CONTEXT.getBean(BEAN_NAME);
        }else{
            SmsSetting smsSetting = smsSettingService.getSmsSettingInfoByParam(SmsSettingQueryRequest.builder().status(0).delFlag(DeleteFlag.NO).build());
            if(smsSetting==null){
                log.error("无短信平台启用");
                return null;
            }
            String businessBeanName = SmsBusinessConstant.BUSINESS_MAPPING.get(smsSetting.getType().toString());
            if(StringUtils.isBlank(businessBeanName)){
                log.error("该短信平台无实现类");
            }
            ApplicationConfig.CONTEXT.removeBeanDefinition(BEAN_NAME);
            ApplicationConfig.CONTEXT.getBeanFactory().registerSingleton(BEAN_NAME,ApplicationConfig.CONTEXT.getBean(businessBeanName));
            return (SmsBaseService) ApplicationConfig.CONTEXT.getBean(BEAN_NAME);
        }*/

        SmsSetting smsSetting = smsSettingService.getSmsSettingInfoByParam(SmsSettingQueryRequest.builder().status(EnableStatus.ENABLE).delFlag(DeleteFlag.NO).build());
        if(smsSetting==null){
            log.error("无短信平台启用");
            throw new SbcRuntimeException(SmsErrorCode.UNABLE_BUSINESS_SETTING);
        }
        String businessBeanName = SmsBusinessConstant.BUSINESS_MAPPING.get(String.valueOf(smsSetting.getType().toValue()));
        if(StringUtils.isBlank(businessBeanName)){
            log.error("该短信平台无实现类");
            throw new SbcRuntimeException(SmsErrorCode.NO_BUSINESS_SERVICE);
        }
        return (SmsBaseService) ApplicationConfig.CONTEXT.getBean(businessBeanName);
    }

    public SmsBaseResponse addSmsSign(SmsSign smsSign){
        return  getBean().addSmsSign(smsSign);
    }
    public SmsBaseResponse modifySmsSign(SmsSign smsSign){
        return  getBean().modifySmsSign(smsSign);
    }

    public SmsBaseResponse deleteSmsSign(SmsSign smsSign){
        return  getBean().deleteSmsSign(smsSign);
    }

    public SmsBaseResponse querySmsSign(SmsSign smsSign){
        return  getBean().querySmsSign(smsSign);
    }

    public SmsBaseResponse addSmsTemplate(SmsTemplate smsTemplate){
        return  getBean().addSmsTemplate(smsTemplate);
    }

    public SmsBaseResponse modifySmsTemplate(SmsTemplate smsTemplate){
        return  getBean().modifySmsTemplate(smsTemplate);
    }

    public SmsBaseResponse deleteSmsTemplate(SmsTemplate smsTemplate){
        return  getBean().deleteSmsTemplate(smsTemplate);
    }

    public SmsBaseResponse querySmsTemplate(SmsTemplate smsTemplate){
        return  getBean().querySmsTemplate(smsTemplate);
    }

    public SmsBaseResponse sendSms(SmsSendDetail smsSendDetail){
        return getBean().sendSms(smsSendDetail);
    }
}
