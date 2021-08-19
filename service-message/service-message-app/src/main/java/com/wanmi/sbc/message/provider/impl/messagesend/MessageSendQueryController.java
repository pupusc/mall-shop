package com.wanmi.sbc.message.provider.impl.messagesend;

import com.wanmi.sbc.message.bean.vo.MessageSendCustomerScopeVO;
import com.wanmi.sbc.message.messagesend.model.root.MessageSendCustomerScope;
import com.wanmi.sbc.message.messagesend.service.MessageSendCustomerScopeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.messagesend.MessageSendQueryProvider;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendPageRequest;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendQueryRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendPageResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendListRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendListResponse;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendByIdRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendByIdResponse;
import com.wanmi.sbc.message.bean.vo.MessageSendVO;
import com.wanmi.sbc.message.messagesend.service.MessageSendService;
import com.wanmi.sbc.message.messagesend.model.root.MessageSend;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>站内信任务表查询服务接口实现</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@RestController
@Validated
public class MessageSendQueryController implements MessageSendQueryProvider {
	@Autowired
	private MessageSendService messageSendService;

	@Autowired
	private MessageSendCustomerScopeService messageSendCustomerScopeService;

	@Override
	public BaseResponse<MessageSendPageResponse> page(@RequestBody @Valid MessageSendPageRequest messageSendPageReq) {
		MessageSendQueryRequest queryReq = KsBeanUtil.convert(messageSendPageReq, MessageSendQueryRequest.class);
		Page<MessageSend> messageSendPage = messageSendService.page(queryReq);
		Page<MessageSendVO> newPage = messageSendPage.map(entity -> {
			MessageSendVO messageSendVO = messageSendService.wrapperVo(entity);
			List<MessageSendCustomerScope> scopes = messageSendCustomerScopeService.getByMessageId(messageSendVO.getMessageId());
			if(CollectionUtils.isNotEmpty(scopes)){
				messageSendVO.setScopeVOList(KsBeanUtil.convertList(scopes, MessageSendCustomerScopeVO.class));
			}
			return messageSendVO;
		});
		MicroServicePage<MessageSendVO> microPage = new MicroServicePage<>(newPage, messageSendPageReq.getPageable());
		MessageSendPageResponse finalRes = new MessageSendPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<MessageSendListResponse> list(@RequestBody @Valid MessageSendListRequest messageSendListReq) {
		MessageSendQueryRequest queryReq = KsBeanUtil.convert(messageSendListReq, MessageSendQueryRequest.class);
		List<MessageSend> messageSendList = messageSendService.list(queryReq);
		List<MessageSendVO> newList = messageSendList.stream().map(entity -> messageSendService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new MessageSendListResponse(newList));
	}

	@Override
	public BaseResponse<MessageSendByIdResponse> getById(@RequestBody @Valid MessageSendByIdRequest messageSendByIdRequest) {
		MessageSend messageSend = messageSendService.getOne(messageSendByIdRequest.getMessageId());
		List<MessageSendCustomerScope> scopeList = messageSendCustomerScopeService.getByMessageId(messageSendByIdRequest.getMessageId());
		MessageSendByIdResponse response = new MessageSendByIdResponse();
		response.setMessageSendVO(messageSendService.wrapperVo(messageSend));
		response.getMessageSendVO().setScopeVOList(KsBeanUtil.convertList(scopeList, MessageSendCustomerScopeVO.class));
		return BaseResponse.success(response);
	}

}

