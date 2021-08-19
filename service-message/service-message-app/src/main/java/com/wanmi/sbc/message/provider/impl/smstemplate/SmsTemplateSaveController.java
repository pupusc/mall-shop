package com.wanmi.sbc.message.provider.impl.smstemplate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.constant.SmsErrorCode;
import com.wanmi.sbc.message.api.provider.smstemplate.SmsTemplateSaveProvider;
import com.wanmi.sbc.message.api.request.smstemplate.*;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateAddResponse;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateModifyResponse;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smssign.service.SmsSignService;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import com.wanmi.sbc.message.smstemplate.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>短信模板保存服务接口实现</p>
 *
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@RestController
@Validated
public class SmsTemplateSaveController implements SmsTemplateSaveProvider {
    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private SmsSignService smsSignService;

    @Override
    public BaseResponse<SmsTemplateAddResponse> add(@RequestBody @Valid SmsTemplateAddRequest smsTemplateAddRequest) {
        SmsTemplate smsTemplate = new SmsTemplate();
        KsBeanUtil.copyPropertiesThird(smsTemplateAddRequest, smsTemplate);
        return BaseResponse.success(new SmsTemplateAddResponse(
                smsTemplateService.wrapperVo(smsTemplateService.add(smsTemplate))));
    }

    @Override
    public BaseResponse<SmsTemplateModifyResponse> modify(@RequestBody @Valid SmsTemplateModifyRequest smsTemplateModifyRequest) {
        SmsTemplate smsTemplate = new SmsTemplate();
        KsBeanUtil.copyPropertiesThird(smsTemplateModifyRequest, smsTemplate);
        return BaseResponse.success(new SmsTemplateModifyResponse(
                smsTemplateService.wrapperVo(smsTemplateService.modify(smsTemplate))));
    }

    @Override
    public BaseResponse<SmsTemplateModifyResponse> modifySmsTemplateReviewStatusByTemplateCode(@RequestBody @Valid ModifySmsTemplateReviewStatusByTemplateCodeRequest request) {
        SmsTemplate smsTemplate = new SmsTemplate();
        KsBeanUtil.copyPropertiesThird(request, smsTemplate);
        smsTemplateService.modifySmsTemplateReviewStatusByTemplateCode(smsTemplate);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid SmsTemplateDelByIdRequest smsTemplateDelByIdRequest) {
        smsTemplateService.deleteById(smsTemplateDelByIdRequest.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid SmsTemplateDelByIdListRequest smsTemplateDelByIdListRequest) {
        smsTemplateService.deleteByIdList(smsTemplateDelByIdListRequest.getIdList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse synchronizePlatformSmsTemplate() {
        smsTemplateService.synchronizePlatformSmsTemplate();
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse synPlatformHistorySmsTemplate(@RequestBody @Valid SyncPlatformHistorySmsTemplateRequest request) {
        smsTemplateService.synPlatformHistorySmsTemplate(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse upload(@RequestBody @Valid SmsTemplateUploadByIdRequest request) {
        SmsTemplate smsTemplate = smsTemplateService.getById(request.getId());
        //待提交状态
        if (ReviewStatus.WAITSUBMIT.equals(smsTemplate.getReviewStatus())) {
            //验证签名
            if(Objects.isNull(smsTemplate.getSignId())){
                throw new SbcRuntimeException(SmsErrorCode.USED_SMS_SIGN);
            }else{
                SmsSign smsSign = smsSignService.getById(smsTemplate.getSignId());
                if(smsSign == null || (!ReviewStatus.REVIEWPASS.equals(smsSign.getReviewStatus())) || smsSign.getDelFlag().equals(DeleteFlag.YES)) {
                    throw new SbcRuntimeException(SmsErrorCode.USED_SMS_SIGN);
                }
            }
            smsTemplateService.upload(smsTemplate);
        }
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyOpenFlagById(@RequestBody @Valid SmsTemplateModifyOpenFlagByIdRequest request){
        smsTemplateService.modifyOpenFlagById(request.getId(), request.getOpenFlag());
        return BaseResponse.SUCCESSFUL();
    }
}

