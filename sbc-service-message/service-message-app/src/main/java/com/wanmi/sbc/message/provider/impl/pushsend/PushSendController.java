package com.wanmi.sbc.message.provider.impl.pushsend;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.pushsend.PushSendProvider;
import com.wanmi.sbc.message.api.request.pushsend.PushSendAddRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendDelByIdListRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendDelByIdRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendModifyRequest;
import com.wanmi.sbc.message.api.response.pushsend.PushSendAddResponse;
import com.wanmi.sbc.message.api.response.pushsend.PushSendModifyResponse;
import com.wanmi.sbc.message.pushsend.model.root.PushSend;
import com.wanmi.sbc.message.pushsend.service.PushSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员推送信息保存服务接口实现</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@RestController
@Validated
public class PushSendController implements PushSendProvider {
	@Autowired
	private PushSendService pushSendService;

	@Override
	public BaseResponse<PushSendAddResponse> add(@RequestBody @Valid PushSendAddRequest pushSendAddRequest) {
		return BaseResponse.success(new PushSendAddResponse(
				pushSendService.wrapperVo(pushSendService.add(pushSendAddRequest))));
	}

	@Override
	public BaseResponse<PushSendModifyResponse> modify(@RequestBody @Valid PushSendModifyRequest pushSendModifyRequest) {

		return BaseResponse.success(new PushSendModifyResponse(
				pushSendService.wrapperVo(pushSendService.modify(pushSendModifyRequest))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PushSendDelByIdRequest pushSendDelByIdRequest) {
		PushSend pushSend = KsBeanUtil.convert(pushSendDelByIdRequest, PushSend.class);
		pushSend.setDelFlag(DeleteFlag.YES);
		pushSendService.deleteById(pushSend);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid PushSendDelByIdListRequest pushSendDelByIdListRequest) {
		List<PushSend> pushSendList = pushSendDelByIdListRequest.getIdList().stream()
			.map(Id -> {
				PushSend pushSend = KsBeanUtil.convert(Id, PushSend.class);
				pushSend.setDelFlag(DeleteFlag.YES);
				return pushSend;
			}).collect(Collectors.toList());
		pushSendService.deleteByIdList(pushSendList);
		return BaseResponse.SUCCESSFUL();
	}

}

