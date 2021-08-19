package com.wanmi.sbc.message.api.provider.smssenddetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smssenddetail.*;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailAddResponse;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>短信发送保存服务Provider</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsSendDetailSaveProvider")
public interface SmsSendDetailSaveProvider {

	/**
	 * 新增短信发送API
	 *
	 * @author zgl
	 * @param smsSendDetailAddRequest 短信发送新增参数结构 {@link SmsSendDetailAddRequest}
	 * @return 新增的短信发送信息 {@link SmsSendDetailAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssenddetail/add")
	BaseResponse<SmsSendDetailAddResponse> add(@RequestBody @Valid SmsSendDetailAddRequest smsSendDetailAddRequest);

    /**
     * 批量新增短信发送API
     *
     * @author zgl
     * @param request 短信发送批量新增参数结构 {@link SmsSendDetailBatchAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/sms/${application.sms.version}/smssenddetail/batch-add")
    BaseResponse batchAdd(@RequestBody @Valid SmsSendDetailBatchAddRequest request);

	/**
	 * 修改短信发送API
	 *
	 * @author zgl
	 * @param smsSendDetailModifyRequest 短信发送修改参数结构 {@link SmsSendDetailModifyRequest}
	 * @return 修改的短信发送信息 {@link SmsSendDetailModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssenddetail/modify")
	BaseResponse<SmsSendDetailModifyResponse> modify(@RequestBody @Valid SmsSendDetailModifyRequest smsSendDetailModifyRequest);

	/**
	 * 单个删除短信发送API
	 *
	 * @author zgl
	 * @param smsSendDetailDelByIdRequest 单个删除参数结构 {@link SmsSendDetailDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssenddetail/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid SmsSendDetailDelByIdRequest smsSendDetailDelByIdRequest);

	/**
	 * 批量删除短信发送API
	 *
	 * @author zgl
	 * @param smsSendDetailDelByIdListRequest 批量删除参数结构 {@link SmsSendDetailDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssenddetail/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid SmsSendDetailDelByIdListRequest smsSendDetailDelByIdListRequest);

}

