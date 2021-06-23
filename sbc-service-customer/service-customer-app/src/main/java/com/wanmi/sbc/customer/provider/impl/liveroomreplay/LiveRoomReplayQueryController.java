package com.wanmi.sbc.customer.provider.impl.liveroomreplay;

import com.wanmi.sbc.customer.api.request.liveroomreplay.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.liveroomreplay.LiveRoomReplayQueryProvider;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayPageResponse;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayListResponse;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayByIdResponse;
import com.wanmi.sbc.customer.bean.vo.LiveRoomReplayVO;
import com.wanmi.sbc.customer.liveroomreplay.service.LiveRoomReplayService;
import com.wanmi.sbc.customer.liveroomreplay.model.root.LiveRoomReplay;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>直播回放查询服务接口实现</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@RestController
@Validated
public class LiveRoomReplayQueryController implements LiveRoomReplayQueryProvider {
	@Autowired
	private LiveRoomReplayService liveRoomReplayService;

	@Override
	public BaseResponse<LiveRoomReplayPageResponse> page(@RequestBody @Valid LiveRoomReplayPageRequest liveRoomReplayPageReq) {
		LiveRoomReplayQueryRequest queryReq = KsBeanUtil.convert(liveRoomReplayPageReq, LiveRoomReplayQueryRequest.class);
		Page<LiveRoomReplay> liveRoomReplayPage = liveRoomReplayService.page(queryReq);
		Page<LiveRoomReplayVO> newPage = liveRoomReplayPage.map(entity -> liveRoomReplayService.wrapperVo(entity));
		MicroServicePage<LiveRoomReplayVO> microPage = new MicroServicePage<>(newPage, liveRoomReplayPageReq.getPageable());
		LiveRoomReplayPageResponse finalRes = new LiveRoomReplayPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<LiveRoomReplayListResponse> list(@RequestBody @Valid LiveRoomReplayListRequest liveRoomReplayListReq) {
		LiveRoomReplayQueryRequest queryReq = KsBeanUtil.convert(liveRoomReplayListReq, LiveRoomReplayQueryRequest.class);
		List<LiveRoomReplay> liveRoomReplayList = liveRoomReplayService.list(queryReq);
		List<LiveRoomReplayVO> newList = liveRoomReplayList.stream().map(entity -> liveRoomReplayService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new LiveRoomReplayListResponse(newList));
	}

	@Override
	public BaseResponse<LiveRoomReplayByIdResponse> getById(@RequestBody @Valid LiveRoomReplayByIdRequest liveRoomReplayByIdRequest) {
		LiveRoomReplay liveRoomReplay =
		liveRoomReplayService.getOne(liveRoomReplayByIdRequest.getId());
		return BaseResponse.success(new LiveRoomReplayByIdResponse(liveRoomReplayService.wrapperVo(liveRoomReplay)));
	}

	@Override
	public BaseResponse<LiveRoomReplayListResponse> getByRoomId(@RequestBody LiveRoomReplayByRoomIdRequest liveRoomReplayByRoomIdRequest) {
		List<LiveRoomReplay> liveRoomReplayList = liveRoomReplayService.getByRoomId(liveRoomReplayByRoomIdRequest.getRoomId());
		List<LiveRoomReplayVO> newList = liveRoomReplayList.stream().map(entity -> liveRoomReplayService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new LiveRoomReplayListResponse(newList));
	}

}

