package com.wanmi.sbc.message.provider.impl.messagesendnode;

import com.wanmi.sbc.message.api.request.messagesendnode.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.messagesendnode.MessageSendNodeProvider;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodeAddResponse;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodeModifyResponse;
import com.wanmi.sbc.message.messagesendnode.service.MessageSendNodeService;
import com.wanmi.sbc.message.messagesendnode.model.root.MessageSendNode;

import javax.validation.Valid;

/**
 * <p>站内信通知节点表保存服务接口实现</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@RestController
@Validated
public class MessageSendNodeController implements MessageSendNodeProvider {
	@Autowired
	private MessageSendNodeService messageSendNodeService;

	@Override
	public BaseResponse<MessageSendNodeAddResponse> add(@RequestBody @Valid MessageSendNodeAddRequest messageSendNodeAddRequest) {
		MessageSendNode messageSendNode = KsBeanUtil.convert(messageSendNodeAddRequest, MessageSendNode.class);
		return BaseResponse.success(new MessageSendNodeAddResponse(
				messageSendNodeService.wrapperVo(messageSendNodeService.add(messageSendNode))));
	}

	@Override
	public BaseResponse<MessageSendNodeModifyResponse> modify(@RequestBody @Valid MessageSendNodeModifyRequest messageSendNodeModifyRequest) {
		MessageSendNode messageSendNode = KsBeanUtil.convert(messageSendNodeModifyRequest, MessageSendNode.class);
		return BaseResponse.success(new MessageSendNodeModifyResponse(
				messageSendNodeService.wrapperVo(messageSendNodeService.modify(messageSendNode))));
	}

	@Override
	public BaseResponse updateStatus(@RequestBody @Valid MessageSendNodeUpdateStatusRequest request) {
		messageSendNodeService.updateStatus(request.getId());
		return BaseResponse.SUCCESSFUL();
	}


}

