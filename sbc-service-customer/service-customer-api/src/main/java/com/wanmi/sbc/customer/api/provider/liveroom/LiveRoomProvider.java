package com.wanmi.sbc.customer.api.provider.liveroom;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.liveroom.*;
import com.wanmi.sbc.customer.api.response.liveroom.LiveRoomAddResponse;
import com.wanmi.sbc.customer.api.response.liveroom.LiveRoomModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播间保存服务Provider</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@FeignClient(value = "${application.customer.name}", contextId = "LiveRoomProvider")
public interface LiveRoomProvider {

	/**
	 * 新增直播间API
	 *
	 * @author zwb
	 * @param liveRoomAddRequest 直播间新增参数结构 {@link LiveRoomAddRequest}
	 * @return 新增的直播间信息 {@link LiveRoomAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/add")
	BaseResponse<LiveRoomAddResponse> add(@RequestBody @Valid LiveRoomAddRequest liveRoomAddRequest);

	/**
	 * 修改直播间API
	 *
	 * @author zwb
	 * @param liveRoomModifyRequest 直播间修改参数结构 {@link LiveRoomModifyRequest}
	 * @return 修改的直播间信息 {@link LiveRoomModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/modify")
	BaseResponse<LiveRoomModifyResponse> modify(@RequestBody @Valid LiveRoomModifyRequest liveRoomModifyRequest);

	/**
	 * 修改直播间状态
	 * @param liveRoomUpdateRequest
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/update")
	BaseResponse<LiveRoomModifyResponse> update(@RequestBody @Valid LiveRoomUpdateRequest liveRoomUpdateRequest);
	/**
	 * 单个删除直播间API
	 *
	 * @author zwb
	 * @param liveRoomDelByIdRequest 单个删除参数结构 {@link LiveRoomDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid LiveRoomDelByIdRequest liveRoomDelByIdRequest);

	/**
	 * 批量删除直播间API
	 *
	 * @author zwb
	 * @param liveRoomDelByIdListRequest 批量删除参数结构 {@link LiveRoomDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid LiveRoomDelByIdListRequest liveRoomDelByIdListRequest);

	/**
	 * 修改直播间是否推荐
	 *
	 * @author zwb
	 * @param recommendReq 单个删除参数结构 {@link LiveRoomDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/recommend")
    BaseResponse recommend(LiveRoomByRoomIdRequest recommendReq);
}

