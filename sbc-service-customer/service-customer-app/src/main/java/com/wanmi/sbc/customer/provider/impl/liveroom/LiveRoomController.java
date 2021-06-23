package com.wanmi.sbc.customer.provider.impl.liveroom;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.liveroom.LiveRoomProvider;
import com.wanmi.sbc.customer.api.request.liveroom.*;
import com.wanmi.sbc.customer.api.response.liveroom.LiveRoomAddResponse;
import com.wanmi.sbc.customer.api.response.liveroom.LiveRoomModifyResponse;
import com.wanmi.sbc.customer.liveroom.model.root.LiveRoom;
import com.wanmi.sbc.customer.liveroom.service.LiveRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>直播间保存服务接口实现</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@RestController
@Validated
public class LiveRoomController implements LiveRoomProvider {
	@Autowired
	private LiveRoomService liveRoomService;

	@Override
	public BaseResponse<LiveRoomAddResponse> add(@RequestBody @Valid LiveRoomAddRequest liveRoomAddRequest) {
		String accessToken = liveRoomAddRequest.getAccessToken();
		LiveRoom liveRoom = KsBeanUtil.convert(liveRoomAddRequest, LiveRoom.class);
		return BaseResponse.success(new LiveRoomAddResponse(
				liveRoomService.wrapperVo(liveRoomService.add(liveRoom,accessToken))));
	}


	@Override
	public BaseResponse<LiveRoomModifyResponse> modify(@RequestBody @Valid LiveRoomModifyRequest liveRoomModifyRequest) {
		LiveRoom liveRoom = KsBeanUtil.convert(liveRoomModifyRequest, LiveRoom.class);
		return BaseResponse.success(new LiveRoomModifyResponse(
				liveRoomService.wrapperVo(liveRoomService.modify(liveRoom))));
	}

	@Override
	public BaseResponse update(@RequestBody @Valid LiveRoomUpdateRequest liveRoomUpdateRequest) {
		liveRoomService.update(liveRoomUpdateRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid LiveRoomDelByIdRequest liveRoomDelByIdRequest) {
		LiveRoom liveRoom = KsBeanUtil.convert(liveRoomDelByIdRequest, LiveRoom.class);
		liveRoom.setDelFlag(DeleteFlag.YES);
		liveRoomService.deleteById(liveRoom);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid LiveRoomDelByIdListRequest liveRoomDelByIdListRequest) {
		List<LiveRoom> liveRoomList = liveRoomDelByIdListRequest.getIdList().stream()
			.map(Id -> {
				LiveRoom liveRoom = KsBeanUtil.convert(Id, LiveRoom.class);
				liveRoom.setDelFlag(DeleteFlag.YES);
				return liveRoom;
			}).collect(Collectors.toList());
		liveRoomService.deleteByIdList(liveRoomList);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse recommend(@RequestBody  LiveRoomByRoomIdRequest recommendReq) {
		liveRoomService.recommend(recommendReq.getRecommend(),recommendReq.getRoomId());
		return BaseResponse.SUCCESSFUL();
	}

}

