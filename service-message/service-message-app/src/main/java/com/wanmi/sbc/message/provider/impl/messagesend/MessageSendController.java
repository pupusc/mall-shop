package com.wanmi.sbc.message.provider.impl.messagesend;

import com.wanmi.sbc.message.messagesend.service.MessageSendCustomerScopeService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.provider.messagesend.MessageSendProvider;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendAddRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendAddResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendModifyRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendModifyResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendDelByIdRequest;
import com.wanmi.sbc.message.messagesend.service.MessageSendService;

import javax.validation.Valid;

/**
 * <p>站内信任务表保存服务接口实现</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@RestController
@Validated
public class MessageSendController implements MessageSendProvider {
	@Autowired
	private MessageSendService messageSendService;

	@Autowired
	private MessageSendCustomerScopeService messageSendCustomerScopeService;

	@Override
	public BaseResponse<MessageSendAddResponse> add(@RequestBody @Valid MessageSendAddRequest messageSendAddRequest) {

		return BaseResponse.success(new MessageSendAddResponse(
				messageSendService.wrapperVo(messageSendService.add(messageSendAddRequest))));
	}

	@Override
	public BaseResponse<MessageSendModifyResponse> modify(@RequestBody @Valid MessageSendModifyRequest messageSendModifyRequest) {

		return BaseResponse.success(new MessageSendModifyResponse(
				messageSendService.wrapperVo(messageSendService.modify(messageSendModifyRequest))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid MessageSendDelByIdRequest messageSendDelByIdRequest) {
		if(messageSendDelByIdRequest.getMessageId() != null){
			messageSendService.deleteById(messageSendDelByIdRequest.getMessageId());
		}else if(messageSendDelByIdRequest.getPushId() != null){
			messageSendService.deleteByPushId(messageSendDelByIdRequest.getPushId());
		}
		return BaseResponse.SUCCESSFUL();
	}


}

