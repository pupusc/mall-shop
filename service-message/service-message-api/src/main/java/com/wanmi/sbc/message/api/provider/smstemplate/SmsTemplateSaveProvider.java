package com.wanmi.sbc.message.api.provider.smstemplate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smstemplate.*;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateAddResponse;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>短信模板保存服务Provider</p>
 *
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsTemplateSaveProvider")
public interface SmsTemplateSaveProvider {

    /**
     * 新增短信模板API
     *
     * @param smsTemplateAddRequest 短信模板新增参数结构 {@link SmsTemplateAddRequest}
     * @return 新增的短信模板信息 {@link SmsTemplateAddResponse}
     * @author lvzhenwei
     */
    @PostMapping("/sms/${application.sms.version}/smstemplate/add")
    BaseResponse<SmsTemplateAddResponse> add(@RequestBody @Valid SmsTemplateAddRequest smsTemplateAddRequest);

    /**
     * 修改短信模板API
     *
     * @param smsTemplateModifyRequest 短信模板修改参数结构 {@link SmsTemplateModifyRequest}
     * @return 修改的短信模板信息 {@link SmsTemplateModifyResponse}
     * @author lvzhenwei
     */
    @PostMapping("/sms/${application.sms.version}/smstemplate/modify")
    BaseResponse<SmsTemplateModifyResponse> modify(@RequestBody @Valid SmsTemplateModifyRequest smsTemplateModifyRequest);

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateModifyResponse>
     * @Author lvzhenwei
     * @Description 根据模板名称更新模板审核状态
     * @Date 16:49 2019/12/6
     * @Param [smsTemplateModifyRequest]
     **/
    @PostMapping("/sms/${application.sms.version}/smstemplate/modifySmsTemplateReviewStatusByTemplateCode")
    BaseResponse<SmsTemplateModifyResponse> modifySmsTemplateReviewStatusByTemplateCode(@RequestBody @Valid ModifySmsTemplateReviewStatusByTemplateCodeRequest request);

    /**
     * 单个删除短信模板API
     *
     * @param smsTemplateDelByIdRequest 单个删除参数结构 {@link SmsTemplateDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author lvzhenwei
     */
    @PostMapping("/sms/${application.sms.version}/smstemplate/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid SmsTemplateDelByIdRequest smsTemplateDelByIdRequest);

    /**
     * 批量删除短信模板API
     *
     * @param smsTemplateDelByIdListRequest 批量删除参数结构 {@link SmsTemplateDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author lvzhenwei
     */
    @PostMapping("/sms/${application.sms.version}/smstemplate/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid SmsTemplateDelByIdListRequest smsTemplateDelByIdListRequest);

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 短信平台短信模板同步
     * @Date 15:23 2019/12/11
     * @Param []
     **/
    @PostMapping("/sms/${application.sms.version}/smstemplate/synchronize-platform-sms-template")
    BaseResponse synchronizePlatformSmsTemplate();

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 同步短信平台短信模板数据
     * @Date 15:23 2019/12/11
     * @Param []
     **/
    @PostMapping("/sms/${application.sms.version}/smstemplate/sync-platform-history-sms-template")
    BaseResponse synPlatformHistorySmsTemplate(@RequestBody @Valid SyncPlatformHistorySmsTemplateRequest request);

    /**
     * 提交备案短信模板API
     *
     * @param request 短信模板新增参数结构 {@link SmsTemplateUploadByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     * @author dyt
     */
    @PostMapping("/sms/${application.sms.version}/smstemplate/upload")
    BaseResponse upload(@RequestBody @Valid SmsTemplateUploadByIdRequest request);

    /**
     * 开关短信模板API
     *
     * @param request 单个短信模板开关参数结构 {@link SmsTemplateModifyOpenFlagByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     * @author dyt
     */
    @PostMapping("/sms/${application.sms.version}/smstemplate/modify-open-flag-by-id")
    BaseResponse modifyOpenFlagById(@RequestBody @Valid SmsTemplateModifyOpenFlagByIdRequest request);
}

