package com.wanmi.sbc.message.api.provider.messagesend;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendPageRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendPageResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendListRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendListResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendByIdRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>站内信任务表查询服务Provider</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@FeignClient(value = "${application.message.name}", contextId = "MessageSendQueryProvider")
public interface MessageSendQueryProvider {

	/**
	 * 分页查询站内信任务表API
	 *
	 * @author xuyunpeng
	 * @param messageSendPageReq 分页请求参数和筛选对象 {@link MessageSendPageRequest}
	 * @return 站内信任务表分页列表信息 {@link MessageSendPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesend/page")
	BaseResponse<MessageSendPageResponse> page(@RequestBody @Valid MessageSendPageRequest messageSendPageReq);

	/**
	 * 列表查询站内信任务表API
	 *
	 * @author xuyunpeng
	 * @param messageSendListReq 列表请求参数和筛选对象 {@link MessageSendListRequest}
	 * @return 站内信任务表的列表信息 {@link MessageSendListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesend/list")
	BaseResponse<MessageSendListResponse> list(@RequestBody @Valid MessageSendListRequest messageSendListReq);

	/**
	 * 单个查询站内信任务表API
	 *
	 * @author xuyunpeng
	 * @param messageSendByIdRequest 单个查询站内信任务表请求参数 {@link MessageSendByIdRequest}
	 * @return 站内信任务表详情 {@link MessageSendByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesend/get-by-id")
	BaseResponse<MessageSendByIdResponse> getById(@RequestBody @Valid MessageSendByIdRequest messageSendByIdRequest);

}

