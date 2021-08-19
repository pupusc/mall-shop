package com.wanmi.sbc.message.provider.impl.messagesendnode;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.messagesendnode.MessageSendNodeQueryProvider;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodePageRequest;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodeQueryRequest;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodePageResponse;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodeByIdRequest;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodeByIdResponse;
import com.wanmi.sbc.message.bean.vo.MessageSendNodeVO;
import com.wanmi.sbc.message.messagesendnode.service.MessageSendNodeService;
import com.wanmi.sbc.message.messagesendnode.model.root.MessageSendNode;
import javax.validation.Valid;

/**
 * <p>站内信通知节点表查询服务接口实现</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@RestController
@Validated
public class MessageSendNodeQueryController implements MessageSendNodeQueryProvider {
	@Autowired
	private MessageSendNodeService messageSendNodeService;

	@Override
	public BaseResponse<MessageSendNodePageResponse> page(@RequestBody @Valid MessageSendNodePageRequest messageSendNodePageReq) {
		MessageSendNodeQueryRequest queryReq = KsBeanUtil.convert(messageSendNodePageReq, MessageSendNodeQueryRequest.class);
		Page<MessageSendNode> messageSendNodePage = messageSendNodeService.page(queryReq);
		Page<MessageSendNodeVO> newPage = messageSendNodePage.map(entity -> messageSendNodeService.wrapperVo(entity));
		MicroServicePage<MessageSendNodeVO> microPage = new MicroServicePage<>(newPage, messageSendNodePageReq.getPageable());
		MessageSendNodePageResponse finalRes = new MessageSendNodePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<MessageSendNodeByIdResponse> getById(@RequestBody @Valid MessageSendNodeByIdRequest messageSendNodeByIdRequest) {
		MessageSendNode messageSendNode = messageSendNodeService.getOne(messageSendNodeByIdRequest.getId());
		return BaseResponse.success(new MessageSendNodeByIdResponse(messageSendNodeService.wrapperVo(messageSendNode)));
	}

}

