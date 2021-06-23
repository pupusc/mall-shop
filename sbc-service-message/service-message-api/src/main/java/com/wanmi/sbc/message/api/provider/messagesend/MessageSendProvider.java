package com.wanmi.sbc.message.api.provider.messagesend;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendAddRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendAddResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendModifyRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendModifyResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendDelByIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>站内信任务表保存服务Provider</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@FeignClient(value = "${application.message.name}", contextId = "MessageSendProvider")
public interface MessageSendProvider {

	/**
	 * 新增站内信任务表API
	 *
	 * @author xuyunpeng
	 * @param messageSendAddRequest 站内信任务表新增参数结构 {@link MessageSendAddRequest}
	 * @return 新增的站内信任务表信息 {@link MessageSendAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesend/add")
	BaseResponse<MessageSendAddResponse> add(@RequestBody @Valid MessageSendAddRequest messageSendAddRequest);

	/**
	 * 修改站内信任务表API
	 *
	 * @author xuyunpeng
	 * @param messageSendModifyRequest 站内信任务表修改参数结构 {@link MessageSendModifyRequest}
	 * @return 修改的站内信任务表信息 {@link MessageSendModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesend/modify")
	BaseResponse<MessageSendModifyResponse> modify(@RequestBody @Valid MessageSendModifyRequest messageSendModifyRequest);

	/**
	 * 单个删除站内信任务表API
	 *
	 * @author xuyunpeng
	 * @param messageSendDelByIdRequest 单个删除参数结构 {@link MessageSendDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesend/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid MessageSendDelByIdRequest messageSendDelByIdRequest);


}

