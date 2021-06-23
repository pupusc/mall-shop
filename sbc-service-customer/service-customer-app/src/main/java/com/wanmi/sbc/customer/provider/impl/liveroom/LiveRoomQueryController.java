package com.wanmi.sbc.customer.provider.impl.liveroom;

import com.wanmi.sbc.customer.api.request.liveroom.*;
import com.wanmi.sbc.customer.api.response.liveroom.*;
import com.wanmi.sbc.customer.bean.vo.LiveRoomByWeChatVO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.liveroom.LiveRoomQueryProvider;
import com.wanmi.sbc.customer.bean.vo.LiveRoomVO;
import com.wanmi.sbc.customer.liveroom.service.LiveRoomService;
import com.wanmi.sbc.customer.liveroom.model.root.LiveRoom;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>直播间查询服务接口实现</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@RestController
@Validated
public class LiveRoomQueryController implements LiveRoomQueryProvider {
	@Autowired
	private LiveRoomService liveRoomService;

	@Override
	public BaseResponse<LiveRoomPageResponse> page(@RequestBody @Valid LiveRoomPageRequest liveRoomPageReq) {
		LiveRoomQueryRequest queryReq = KsBeanUtil.convert(liveRoomPageReq, LiveRoomQueryRequest.class);
		Page<LiveRoom> liveRoomPage = liveRoomService.page(queryReq);
		Page<LiveRoomVO> newPage = liveRoomPage.map(entity -> liveRoomService.wrapperVo(entity));
		MicroServicePage<LiveRoomVO> microPage = new MicroServicePage<>(newPage, liveRoomPageReq.getPageable());
		LiveRoomPageResponse finalRes = new LiveRoomPageResponse();
		finalRes.setLiveRoomVOPage(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<LiveRoomListResponse> list(@RequestBody @Valid LiveRoomListRequest liveRoomListReq) {
		LiveRoomQueryRequest queryReq = KsBeanUtil.convert(liveRoomListReq, LiveRoomQueryRequest.class);
		List<LiveRoom> liveRoomList = liveRoomService.list(queryReq);
		List<LiveRoomVO> newList = liveRoomList.stream().map(entity -> liveRoomService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new LiveRoomListResponse(newList));
	}

	@Override
	public BaseResponse<LiveRoomByIdResponse> getById(@RequestBody @Valid LiveRoomByIdRequest liveRoomByIdRequest) {
		LiveRoom liveRoom =
		liveRoomService.getOne(liveRoomByIdRequest.getId());
		LiveRoomVO liveRoomVO = liveRoomService.wrapperVo(liveRoom);
		LiveRoomByIdResponse response = new LiveRoomByIdResponse();
		response.setLiveRoomVO(liveRoomVO);
		return BaseResponse.success(response);
	}

	@Override
	public BaseResponse<LiveRoomPageAllResponse> pageNew(@RequestBody @Valid LiveRoomPageRequest liveRoomPageReq) {
		LiveRoomQueryRequest queryReq = KsBeanUtil.convert(liveRoomPageReq, LiveRoomQueryRequest.class);
		LiveRoomPageAllResponse allResponse = liveRoomService.pageNew(queryReq);
		return BaseResponse.success(allResponse);
	}



}

