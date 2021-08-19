package com.wanmi.sbc.customer.api.provider.liveroomreplay;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayAddRequest;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayAddResponse;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayModifyRequest;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayModifyResponse;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayDelByIdRequest;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播回放保存服务Provider</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@FeignClient(value = "${application.customer.name}", contextId = "LiveRoomReplayProvider")
public interface LiveRoomReplayProvider {

	/**
	 * 新增直播回放API
	 *
	 * @author zwb
	 * @param liveRoomReplayAddRequest 直播回放新增参数结构 {@link LiveRoomReplayAddRequest}
	 * @return 新增的直播回放信息 {@link LiveRoomReplayAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroomreplay/add")
	BaseResponse<LiveRoomReplayAddResponse> add(@RequestBody @Valid LiveRoomReplayAddRequest liveRoomReplayAddRequest);

	/**
	 * 修改直播回放API
	 *
	 * @author zwb
	 * @param liveRoomReplayModifyRequest 直播回放修改参数结构 {@link LiveRoomReplayModifyRequest}
	 * @return 修改的直播回放信息 {@link LiveRoomReplayModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroomreplay/modify")
	BaseResponse<LiveRoomReplayModifyResponse> modify(@RequestBody @Valid LiveRoomReplayModifyRequest liveRoomReplayModifyRequest);

	/**
	 * 单个删除直播回放API
	 *
	 * @author zwb
	 * @param liveRoomReplayDelByIdRequest 单个删除参数结构 {@link LiveRoomReplayDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroomreplay/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid LiveRoomReplayDelByIdRequest liveRoomReplayDelByIdRequest);

	/**
	 * 批量删除直播回放API
	 *
	 * @author zwb
	 * @param liveRoomReplayDelByIdListRequest 批量删除参数结构 {@link LiveRoomReplayDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroomreplay/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid LiveRoomReplayDelByIdListRequest liveRoomReplayDelByIdListRequest);

}

