package com.wanmi.sbc.message.business.tencentsms;

import com.wanmi.sbc.message.SmsBaseResponse;
import com.wanmi.sbc.message.SmsBaseService;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingQueryRequest;
import com.wanmi.sbc.message.bean.enums.SmsSettingType;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import com.wanmi.sbc.message.smssetting.service.SmsSettingService;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName TencentSmsService
 * @Description Tencent短信平台接口调用
 * @Author zgl
 * @Date 2019/12/4 14:25
 **/
@Service("tencentSmsService")
public class TencentSmsService implements SmsBaseService {

    @Autowired
    private SmsSettingService smsSettingService;

    /**
     * @Author zgl
     * @Description Tencent短信平台接口--新增短信签名接口t
     * @Date 14:26 2019/12/4
     * @Param [smsSignQueryRequest]
     * @return SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse addSmsSign(SmsSign smsSign){
        return client();
    }

    /**
     * @Author zgl
     * @Description Tencent短信平台接口--编辑短信签名接口
     * @Date 14:26 2019/12/4
     * @Param [smsSignQueryRequest]
     * @return SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse modifySmsSign(SmsSign smsSign){
       ;
        return client();
    }

    /**
     * @Author zgl
     * @Description Tencent短信平台接口--删除短信签名接口
     * @Date 14:26 2019/12/4
     * @Param [smsSignQueryRequest]
     * @return SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse deleteSmsSign(SmsSign smsSign){

        return client();
    }

    @Override
    public SmsBaseResponse querySmsSign(SmsSign smsSign) {
        return null;
    }

    /**
     * @Author zgl
     * @Description Tencent短信平台接口--新增短信模板接口
     * @Date 14:26 2019/12/4
     * @Param [smsTemplateByIdRequest]
     * @return SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse addSmsTemplate(SmsTemplate smsTemplate){

        return client();
    }

    /**
     * @Author zgl
     * @Description Tencent短信平台接口--新增短信模板接口
     * @Date 14:26 2019/12/4
     * @Param [smsTemplateByIdRequest]
     * @return SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse modifySmsTemplate(SmsTemplate smsTemplate){

        return client();
    }

    /**
     * @Author zgl
     * @Description Tencent短信平台接口--新增短信模板接口
     * @Date 14:26 2019/12/4
     * @Param [smsTemplateByIdRequest]
     * @return SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse deleteSmsTemplate(SmsTemplate smsTemplate){

        return client();
    }

    @Override
    public SmsBaseResponse querySmsTemplate(SmsTemplate smsTemplate) {
        return null;
    }

    @Override
    public SmsBaseResponse sendSms(SmsSendDetail smsSendDetail){
        return client();
    }

    /**
     * @Author zgl
     * @Description 调用腾讯云短信平台接口统一入口
     * @Date 14:26 2019/12/4
     * @Param [request]
     * @return com.Tencentcs.CommonResponse
     **/
    private SmsBaseResponse client(){
        SmsSetting smsSetting = smsSettingService.getSmsSettingInfoByParam(SmsSettingQueryRequest.builder().type(SmsSettingType.ALIYUN).build());

        return SmsBaseResponse.builder().Code("error").build();
    }
}
