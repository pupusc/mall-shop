package com.wanmi.sbc.message.api.provider.messagesendnode;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodePageRequest;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodePageResponse;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodeByIdRequest;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodeByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>站内信通知节点表查询服务Provider</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@FeignClient(value = "${application.message.name}", contextId = "MessageSendNodeQueryProvider")
public interface MessageSendNodeQueryProvider {

	/**
	 * 分页查询站内信通知节点表API
	 *
	 * @author xuyunpeng
	 * @param messageSendNodePageReq 分页请求参数和筛选对象 {@link MessageSendNodePageRequest}
	 * @return 站内信通知节点表分页列表信息 {@link MessageSendNodePageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesendnode/page")
	BaseResponse<MessageSendNodePageResponse> page(@RequestBody @Valid MessageSendNodePageRequest messageSendNodePageReq);

	/**
	 * 单个查询站内信通知节点表API
	 *
	 * @author xuyunpeng
	 * @param messageSendNodeByIdRequest 单个查询站内信通知节点表请求参数 {@link MessageSendNodeByIdRequest}
	 * @return 站内信通知节点表详情 {@link MessageSendNodeByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesendnode/get-by-id")
	BaseResponse<MessageSendNodeByIdResponse> getById(@RequestBody @Valid MessageSendNodeByIdRequest messageSendNodeByIdRequest);

}

