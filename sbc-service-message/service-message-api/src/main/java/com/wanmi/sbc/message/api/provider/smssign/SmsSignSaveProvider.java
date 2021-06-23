package com.wanmi.sbc.message.api.provider.smssign;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smssign.*;
import com.wanmi.sbc.message.api.response.smssign.SmsSignAddResponse;
import com.wanmi.sbc.message.api.response.smssign.SmsSignModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>短信签名保存服务Provider</p>
 *
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsSignSaveProvider")
public interface SmsSignSaveProvider {

    /**
     * 新增短信签名API
     *
     * @param smsSignAddRequest 短信签名新增参数结构 {@link SmsSignAddRequest}
     * @return 新增的短信签名信息 {@link SmsSignAddResponse}
     * @author lvzhenwei
     */
    @PostMapping("/sms/${application.sms.version}/smssign/add")
    BaseResponse<SmsSignAddResponse> add(@RequestBody @Valid SmsSignAddRequest smsSignAddRequest);

    /**
     * 修改短信签名API
     *
     * @param smsSignModifyRequest 短信签名修改参数结构 {@link SmsSignModifyRequest}
     * @return 修改的短信签名信息 {@link SmsSignModifyResponse}
     * @author lvzhenwei
     */
    @PostMapping("/sms/${application.sms.version}/smssign/modify")
    BaseResponse<SmsSignModifyResponse> modify(@RequestBody @Valid SmsSignModifyRequest smsSignModifyRequest);

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.message.api.response.smssign.SmsSignModifyResponse>
     * @Author lvzhenwei
     * @Description 根据模板名称更新模板审核状态
     * @Date 16:29 2019/12/6
     * @Param [smsSignModifyRequest]
     **/
    @PostMapping("/sms/${application.sms.version}/smssign/modify-review-status-byname")
    BaseResponse<SmsSignModifyResponse> modifyReviewStatusByName(@RequestBody @Valid ModifyReviewStatusByNameRequest request);

    /**
     * 单个删除短信签名API
     *
     * @param smsSignDelByIdRequest 单个删除参数结构 {@link SmsSignDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author lvzhenwei
     */
    @PostMapping("/sms/${application.sms.version}/smssign/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid SmsSignDelByIdRequest smsSignDelByIdRequest);

    /**
     * 批量删除短信签名API
     *
     * @param smsSignDelByIdListRequest 批量删除参数结构 {@link SmsSignDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author lvzhenwei
     */
    @PostMapping("/sms/${application.sms.version}/smssign/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid SmsSignDelByIdListRequest smsSignDelByIdListRequest);

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 短信平台短信签名同步API
     * @Date 14:20 2019/12/11
     * @Param []
     **/
    @PostMapping("/sms/${application.sms.version}/smssign/synchronize-platform-smsSign")
    BaseResponse synchronizePlatformSmsSign();

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 同步短信平台历史短信签名数据
     * @Date 17:18 2019/12/11
     * @Param [syncSmsSignByNamesRequest]
     **/
    @PostMapping("/sms/${application.sms.version}/smssign/sync-platform-history-smsSign-by-names")
    BaseResponse syncPlatformHistorySmsSignByNames(@RequestBody @Valid SyncSmsSignByNamesRequest syncSmsSignByNamesRequest);

}

