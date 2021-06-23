package com.wanmi.sbc.message.provider.impl.smssign;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.smssign.SmsSignSaveProvider;
import com.wanmi.sbc.message.api.request.smssend.SmsSendQueryRequest;
import com.wanmi.sbc.message.api.request.smssign.*;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateQueryRequest;
import com.wanmi.sbc.message.api.response.smssign.SmsSignAddResponse;
import com.wanmi.sbc.message.api.response.smssign.SmsSignModifyResponse;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import com.wanmi.sbc.message.smssend.service.SmsSendService;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import com.wanmi.sbc.message.smssign.service.SmsSignService;
import com.wanmi.sbc.message.smstemplate.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>短信签名保存服务接口实现</p>
 *
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@RestController
@Validated
public class SmsSignSaveController implements SmsSignSaveProvider {
    @Autowired
    private SmsSignService smsSignService;

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private SmsSendService smsSendService;

    @Override
    public BaseResponse<SmsSignAddResponse> add(@RequestBody @Valid SmsSignAddRequest smsSignAddRequest) {
        SmsSign smsSign = KsBeanUtil.convert(smsSignAddRequest, SmsSign.class);
        return BaseResponse.success(new SmsSignAddResponse(
                smsSignService.wrapperVo(smsSignService.add(smsSign))));
    }

    @Override
    public BaseResponse<SmsSignModifyResponse> modify(@RequestBody @Valid SmsSignModifyRequest smsSignModifyRequest) {
        SmsSign smsSign = KsBeanUtil.convert(smsSignModifyRequest, SmsSign.class);
        return BaseResponse.success(new SmsSignModifyResponse(
                smsSignService.wrapperVo(smsSignService.modify(smsSign))));
    }

    @Override
    public BaseResponse<SmsSignModifyResponse> modifyReviewStatusByName(@RequestBody @Valid ModifyReviewStatusByNameRequest request) {
        SmsSign smsSign = KsBeanUtil.convert(request, SmsSign.class);
        smsSignService.modifyReviewStatusByName(smsSign);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid SmsSignDelByIdRequest smsSignDelByIdRequest) {
        //验证未删除模板、未结束的任务是否存在使用
        if (smsTemplateService.count(SmsTemplateQueryRequest.builder().signId(smsSignDelByIdRequest.getId())
                .delFlag(DeleteFlag.NO).build()) > 0
                || smsSendService.count(SmsSendQueryRequest.builder().signId(smsSignDelByIdRequest.getId())
                .noStatus(SendStatus.END).build()) > 0) {
            throw new SbcRuntimeException("K-300204");
        }
        smsSignService.deleteById(smsSignDelByIdRequest.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid SmsSignDelByIdListRequest smsSignDelByIdListRequest) {
        smsSignService.deleteByIdList(smsSignDelByIdListRequest.getIdList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse synchronizePlatformSmsSign() {
        smsSignService.synchronizePlatformSmsSign();
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse syncPlatformHistorySmsSignByNames(@RequestBody @Valid SyncSmsSignByNamesRequest syncSmsSignByNamesRequest) {
        smsSignService.syncPlatformHistorySmsSignByNames(syncSmsSignByNamesRequest);
        return BaseResponse.SUCCESSFUL();
    }

}

