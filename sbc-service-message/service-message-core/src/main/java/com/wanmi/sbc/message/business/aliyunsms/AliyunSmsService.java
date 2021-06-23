package com.wanmi.sbc.message.business.aliyunsms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.SmsBaseResponse;
import com.wanmi.sbc.message.SmsBaseService;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingQueryRequest;
import com.wanmi.sbc.message.bean.constant.AliyunSmsResponseCodeMapping;
import com.wanmi.sbc.message.bean.constant.SmsResponseCode;
import com.wanmi.sbc.message.bean.enums.SmsSettingType;
import com.wanmi.sbc.message.bean.enums.SmsType;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import com.wanmi.sbc.message.smssetting.service.SmsSettingService;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smssign.service.SmsSignService;
import com.wanmi.sbc.message.smssignfileinfo.model.root.SmsSignFileInfo;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import com.wanmi.sbc.message.smstemplate.repository.SmsTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * @ClassName AliyunSmsService
 * @Description aliyun短信平台接口调用
 * @Author lvzhenwei
 * @Date 2019/12/4 10:01
 **/
@Service("aliyunSmsService")
@Slf4j
public class AliyunSmsService implements SmsBaseService {

    @Autowired
    private SmsSettingService smsSettingService;

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Autowired
    private SmsSignService smsSignService;

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--新增短信签名接口
     * @Date 10:51 2019/12/4
     * @Param [smsSignQueryRequest]
     **/
    @Override
    public SmsBaseResponse addSmsSign(SmsSign smsSign) {
        CommonRequest request = new CommonRequest();
        request.setAction("addSmsSign");
        request.putQueryParameter("SignName", smsSign.getSmsSignName());
        request.putQueryParameter("SignSource", String.valueOf(smsSign.getSignSource().toValue()));
        request.putQueryParameter("Remark", smsSign.getRemark());
        int i = 0;
        for (SmsSignFileInfo smsSignFileInfo : smsSign.getSmsSignFileInfoList()) {
            i++;
            request.putQueryParameter("SignFileList." + i + ".FileSuffix", smsSignFileInfo.getFileName());
            request.putQueryParameter("SignFileList." + i + ".FileContents", smsSignFileInfo.getFileUrl());
        }
        return client(request);
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--编辑短信签名接口
     * @Date 10:51 2019/12/4
     * @Param [smsSignQueryRequest]
     **/
    @Override
    public SmsBaseResponse modifySmsSign(SmsSign smsSign) {
        CommonRequest request = new CommonRequest();
        request.setAction("modifySmsSign");
        request.putQueryParameter("SignName", smsSign.getSmsSignName());
        request.putQueryParameter("SignSource", String.valueOf(smsSign.getSignSource().toValue()));
        request.putQueryParameter("Remark", smsSign.getRemark());
        int i = 0;
        for (SmsSignFileInfo smsSignFileInfo : smsSign.getSmsSignFileInfoList()) {
            i++;
            request.putQueryParameter("SignFileList." + i + ".FileSuffix", smsSignFileInfo.getFileName());
            request.putQueryParameter("SignFileList." + i + ".FileContents", smsSignFileInfo.getFileUrl());
        }
        return client(request);
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--删除短信签名接口
     * @Date 10:51 2019/12/4
     * @Param [smsSignQueryRequest]
     **/
    @Override
    public SmsBaseResponse deleteSmsSign(SmsSign smsSign) {
        CommonRequest request = new CommonRequest();
        request.setAction("deleteSmsSign");
        request.putQueryParameter("SignName", smsSign.getSmsSignName());
        return client(request);
    }

    /**
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--查询短信签名接口
     * @Date 19:31 2019/12/9
     * @Param [smsSign]
     * @return com.wanmi.sbc.message.SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse querySmsSign(SmsSign smsSign){
        CommonRequest request = new CommonRequest();
        request.setAction("QuerySmsSign");
        request.putQueryParameter("SignName", smsSign.getSmsSignName());
        return client(request);
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--新增短信模板接口
     * @Date 10:51 2019/12/4
     * @Param [smsTemplateByIdRequest]
     **/
    @Override
    public SmsBaseResponse addSmsTemplate(SmsTemplate smsTemplate) {
        CommonRequest request = new CommonRequest();
        request.setAction("addSmsTemplate");
        request.putQueryParameter("TemplateType", String.valueOf(smsTemplate.getTemplateType().toValue()));
        request.putQueryParameter("TemplateName", smsTemplate.getTemplateName());
        request.putQueryParameter("TemplateContent", smsTemplate.getTemplateContent());
        request.putQueryParameter("Remark", smsTemplate.getRemark());
        return client(request);
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--新增短信模板接口
     * @Date 10:51 2019/12/4
     * @Param [smsTemplateByIdRequest]
     **/
    @Override
    public SmsBaseResponse modifySmsTemplate(SmsTemplate smsTemplate) {
        CommonRequest request = new CommonRequest();
        request.setAction("modifySmsTemplate");
        request.putQueryParameter("TemplateType", String.valueOf(smsTemplate.getTemplateType().toValue()));
        request.putQueryParameter("TemplateName", smsTemplate.getTemplateName());
        request.putQueryParameter("TemplateContent", smsTemplate.getTemplateContent());
        request.putQueryParameter("Remark", smsTemplate.getRemark());
        request.putQueryParameter("TemplateCode", smsTemplate.getTemplateCode());
        return client(request);
    }

    /**
     * @return com.aliyuncs.CommonResponse
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--删除短信模板接口
     * @Date 10:51 2019/12/4
     * @Param [smsTemplateByIdRequest]
     **/
    @Override
    public SmsBaseResponse deleteSmsTemplate(SmsTemplate smsTemplate) {
        CommonRequest request = new CommonRequest();
        request.setAction("deleteSmsTemplate");
        request.putQueryParameter("TemplateCode", smsTemplate.getTemplateCode());
        return client(request);
    }

    /**
     * @Author lvzhenwei
     * @Description aliyun短信平台接口--查询短信签名接口
     * @Date 19:31 2019/12/9
     * @Param [smsSign]
     * @return com.wanmi.sbc.message.SmsBaseResponse
     **/
    @Override
    public SmsBaseResponse querySmsTemplate(SmsTemplate smsTemplate){
        CommonRequest request = new CommonRequest();
        request.setAction("QuerySmsTemplate");
        request.putQueryParameter("TemplateCode", smsTemplate.getTemplateCode());
        return client(request);
    }

    /**
     * 短信发送接口
     * @param smsSendDetail
     * @return
     */
    @Override
    public SmsBaseResponse sendSms(SmsSendDetail smsSendDetail){
        //根据业务类型获取模板编码
        if (StringUtils.isBlank(smsSendDetail.getTemplateCode())
                && StringUtils.isNotBlank(smsSendDetail.getBusinessType())) {
            SmsTemplate smsTemplate = smsTemplateRepository.findByBusinessTypeAndDelFlag(smsSendDetail.getBusinessType(),
                    DeleteFlag.NO);
            if (Objects.nonNull(smsTemplate)) {
                smsSendDetail.setTemplateCode(smsTemplate.getTemplateCode());
                if (Objects.nonNull(smsTemplate.getSignId())) {
                    smsSendDetail.setSignName(smsSignService.getById(smsTemplate.getSignId()).getSmsSignName());
                }
                //通知类短信验证开关状态是否关闭
                if (SmsType.NOTICE.equals(smsTemplate.getTemplateType())
                        && (Objects.isNull(smsTemplate.getOpenFlag()) || Boolean.FALSE.equals(smsTemplate.getOpenFlag()))) {
                    log.error("短信模板被关闭".concat(smsSendDetail.getBusinessType()));
                    SmsBaseResponse smsBaseResponse = new SmsBaseResponse();
                    smsBaseResponse.setCode(SmsResponseCode.DEFAULT_CONFIG_ERROR);
                    return smsBaseResponse;
                }
            } else {
                log.error("发送短信模板不存在，businessType={}", smsSendDetail.getBusinessType());
                SmsBaseResponse smsBaseResponse = new SmsBaseResponse();
                smsBaseResponse.setCode(SmsResponseCode.DEFAULT_CONFIG_ERROR);
                return smsBaseResponse;
            }
        }
        CommonRequest request = new CommonRequest();
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", smsSendDetail.getPhoneNumbers());
        if (StringUtils.isNotBlank(smsSendDetail.getSignName())) {
            request.putQueryParameter("SignName", smsSendDetail.getSignName());
        } else if (smsSendDetail.getSendId() != null) {
            request.putQueryParameter("SignName", smsSignService.getById(smsSendDetail.getSignId()).getSmsSignName());
        }
        request.putQueryParameter("TemplateCode", smsSendDetail.getTemplateCode());
        if (StringUtils.isNotEmpty(smsSendDetail.getTemplateParam())) {
            request.putQueryParameter("TemplateParam", smsSendDetail.getTemplateParam());
        }
        log.info("发送短信【{}】，mobile={}，templateCode={},sendId={},params={}", smsSendDetail.getSignName(),
                smsSendDetail.getPhoneNumbers(), smsSendDetail.getTemplateCode(),
                Objects.toString(smsSendDetail.getSendId(), ""), Objects.toString(smsSendDetail.getTemplateParam(), ""));
        return client(request);
    }
    /**
     * @Author lvzhenwei
     * @Description 调用阿里云短信平台接口统一入口
     * @Date 10:50 2019/12/4
     * @Param [request]
     * @return com.aliyuncs.CommonResponse
     **/
    private SmsBaseResponse client(CommonRequest request){
        SmsBaseResponse smsBaseResponse = new SmsBaseResponse();
        SmsSetting smsSetting = smsSettingService.getSmsSettingInfoByParam(SmsSettingQueryRequest.builder().type(SmsSettingType.ALIYUN).build());
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsSetting.getAccessKeyId(), smsSetting.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonResponse response = new CommonResponse();
        try {
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            response = client.getCommonResponse(request);
            smsBaseResponse = JSONObject.parseObject(response.getData(),SmsBaseResponse.class);
            String code = StringUtils.isBlank(AliyunSmsResponseCodeMapping.CODE_MAPPING.get(smsBaseResponse.getCode()))
                    ?SmsResponseCode.DEFAULT_ERROR
                    :AliyunSmsResponseCodeMapping.CODE_MAPPING.get(smsBaseResponse.getCode());
            smsBaseResponse.setCode(code);
            if(!SmsResponseCode.SUCCESS.equals(smsBaseResponse.getCode())) {
                log.error("请求阿里云失败, exMsg:{}; costTime:{}", smsBaseResponse.getMessage());
            }
        } catch (ServerException e) {
            log.error("请求阿里云异常", e);
            smsBaseResponse.setCode(SmsResponseCode.DEFAULT_ERROR);
            smsBaseResponse.setMessage(e.getErrMsg());
        } catch (ClientException e) {
            log.error("发送短信异常", e);
            smsBaseResponse.setCode(SmsResponseCode.DEFAULT_ERROR);
            smsBaseResponse.setMessage(e.getErrMsg());
        }

        return smsBaseResponse;
    }
}
