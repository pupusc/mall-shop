package com.wanmi.sbc.message.api.provider.messagesendnode;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.messagesendnode.*;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodeAddResponse;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodeModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>站内信通知节点表保存服务Provider</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@FeignClient(value = "${application.message.name}", contextId = "MessageSendNodeProvider")
public interface MessageSendNodeProvider {

	/**
	 * 新增站内信通知节点表API
	 *
	 * @author xuyunpeng
	 * @param messageSendNodeAddRequest 站内信通知节点表新增参数结构 {@link MessageSendNodeAddRequest}
	 * @return 新增的站内信通知节点表信息 {@link MessageSendNodeAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesendnode/add")
	BaseResponse<MessageSendNodeAddResponse> add(@RequestBody @Valid MessageSendNodeAddRequest messageSendNodeAddRequest);

	/**
	 * 修改站内信通知节点表API
	 *
	 * @author xuyunpeng
	 * @param messageSendNodeModifyRequest 站内信通知节点表修改参数结构 {@link MessageSendNodeModifyRequest}
	 * @return 修改的站内信通知节点表信息 {@link MessageSendNodeModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/messagesendnode/modify")
	BaseResponse<MessageSendNodeModifyResponse> modify(@RequestBody @Valid MessageSendNodeModifyRequest messageSendNodeModifyRequest);

	/**
	 * 站内信通知节点开关API
	 *
	 * @author xuyunpeng
	 * @param
	 * @return
	 */
	@PostMapping("/sms/${application.sms.version}/messagesendnode/update-status")
	BaseResponse updateStatus(@RequestBody @Valid MessageSendNodeUpdateStatusRequest request);


}

