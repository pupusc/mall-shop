package com.wanmi.sbc.customer.provider.impl.liveroomreplay;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.liveroomreplay.LiveRoomReplayProvider;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayAddRequest;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayAddResponse;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayModifyRequest;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayModifyResponse;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayDelByIdRequest;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayDelByIdListRequest;
import com.wanmi.sbc.customer.liveroomreplay.service.LiveRoomReplayService;
import com.wanmi.sbc.customer.liveroomreplay.model.root.LiveRoomReplay;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>直播回放保存服务接口实现</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@RestController
@Validated
public class LiveRoomReplayController implements LiveRoomReplayProvider {
	@Autowired
	private LiveRoomReplayService liveRoomReplayService;

	@Override
	public BaseResponse<LiveRoomReplayAddResponse> add(@RequestBody @Valid LiveRoomReplayAddRequest liveRoomReplayAddRequest) {
		LiveRoomReplay liveRoomReplay = KsBeanUtil.convert(liveRoomReplayAddRequest, LiveRoomReplay.class);
		return BaseResponse.success(new LiveRoomReplayAddResponse(
				liveRoomReplayService.wrapperVo(liveRoomReplayService.add(liveRoomReplay))));
	}

	@Override
	public BaseResponse<LiveRoomReplayModifyResponse> modify(@RequestBody @Valid LiveRoomReplayModifyRequest liveRoomReplayModifyRequest) {
		LiveRoomReplay liveRoomReplay = KsBeanUtil.convert(liveRoomReplayModifyRequest, LiveRoomReplay.class);
		return BaseResponse.success(new LiveRoomReplayModifyResponse(
				liveRoomReplayService.wrapperVo(liveRoomReplayService.modify(liveRoomReplay))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid LiveRoomReplayDelByIdRequest liveRoomReplayDelByIdRequest) {
		LiveRoomReplay liveRoomReplay = KsBeanUtil.convert(liveRoomReplayDelByIdRequest, LiveRoomReplay.class);
		liveRoomReplay.setDelFlag(DeleteFlag.YES);
		liveRoomReplayService.deleteById(liveRoomReplay);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid LiveRoomReplayDelByIdListRequest liveRoomReplayDelByIdListRequest) {
		List<LiveRoomReplay> liveRoomReplayList = liveRoomReplayDelByIdListRequest.getIdList().stream()
			.map(Id -> {
				LiveRoomReplay liveRoomReplay = KsBeanUtil.convert(Id, LiveRoomReplay.class);
				liveRoomReplay.setDelFlag(DeleteFlag.YES);
				return liveRoomReplay;
			}).collect(Collectors.toList());
		liveRoomReplayService.deleteByIdList(liveRoomReplayList);
		return BaseResponse.SUCCESSFUL();
	}

}

