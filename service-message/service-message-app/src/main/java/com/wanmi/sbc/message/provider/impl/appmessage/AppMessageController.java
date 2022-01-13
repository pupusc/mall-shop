package com.wanmi.sbc.message.provider.impl.appmessage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.appmessage.AppMessageProvider;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageAddRequest;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageDelByIdRequest;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageModifyRequest;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageSetReadRequest;
import com.wanmi.sbc.message.api.response.appmessage.AppMessageAddResponse;
import com.wanmi.sbc.message.api.response.appmessage.AppMessageModifyResponse;
import com.wanmi.sbc.message.appmessage.model.root.AppMessage;
import com.wanmi.sbc.message.appmessage.service.AppMessageService;
import com.wanmi.sbc.message.bean.enums.ReadFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>App站内信消息发送表保存服务接口实现</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@RestController
@Validated
public class AppMessageController implements AppMessageProvider {
	@Autowired
	private AppMessageService appMessageService;

	@Override
	public BaseResponse<AppMessageAddResponse> addBatch(@RequestBody @Valid AppMessageAddRequest appMessageAddRequest) {
		return BaseResponse.success(new AppMessageAddResponse(appMessageService.addBatch(appMessageAddRequest)));
	}

	@Override
	public BaseResponse<AppMessageModifyResponse> modify(@RequestBody @Valid AppMessageModifyRequest appMessageModifyRequest) {
		AppMessage appMessage = KsBeanUtil.convert(appMessageModifyRequest, AppMessage.class);
		return BaseResponse.success(new AppMessageModifyResponse(
				appMessageService.wrapperVo(appMessageService.modify(appMessage))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid AppMessageDelByIdRequest appMessageDelByIdRequest) {
		appMessageService.deleteById(appMessageDelByIdRequest.getAppMessageId(), appMessageDelByIdRequest.getCustomerId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse setMessageRead(@Valid @RequestBody AppMessageSetReadRequest request) {
		AppMessage appMessage = appMessageService.getOne(request.getMessageId(), request.getCustomerId());
		if(Objects.nonNull(appMessage) && appMessage.getIsRead() == ReadFlag.NO){
			appMessageService.setMessageRead(request.getCustomerId(), request.getMessageId(),
					appMessage.getMessageType(), appMessage.getJoinId());
		}
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse setMessageAllRead(@Valid @RequestBody AppMessageSetReadRequest request) {
	    appMessageService.setAllMessageRead(request.getCustomerId());
		return BaseResponse.SUCCESSFUL();
	}

}

