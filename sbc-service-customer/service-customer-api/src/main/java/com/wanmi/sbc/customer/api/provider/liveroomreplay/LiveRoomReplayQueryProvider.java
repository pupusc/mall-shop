package com.wanmi.sbc.customer.api.provider.liveroomreplay;

import ch.qos.logback.classic.gaffer.GafferConfigurator;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayByRoomIdRequest;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayPageRequest;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayPageResponse;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayListRequest;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayListResponse;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayByIdRequest;
import com.wanmi.sbc.customer.api.response.liveroomreplay.LiveRoomReplayByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播回放查询服务Provider</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@FeignClient(value = "${application.customer.name}", contextId = "LiveRoomReplayQueryProvider")
public interface LiveRoomReplayQueryProvider {

	/**
	 * 分页查询直播回放API
	 *
	 * @author zwb
	 * @param liveRoomReplayPageReq 分页请求参数和筛选对象 {@link LiveRoomReplayPageRequest}
	 * @return 直播回放分页列表信息 {@link LiveRoomReplayPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroomreplay/page")
	BaseResponse<LiveRoomReplayPageResponse> page(@RequestBody @Valid LiveRoomReplayPageRequest liveRoomReplayPageReq);

	/**
	 * 列表查询直播回放API
	 *
	 * @author zwb
	 * @param liveRoomReplayListReq 列表请求参数和筛选对象 {@link LiveRoomReplayListRequest}
	 * @return 直播回放的列表信息 {@link LiveRoomReplayListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroomreplay/list")
	BaseResponse<LiveRoomReplayListResponse> list(@RequestBody @Valid LiveRoomReplayListRequest liveRoomReplayListReq);

	/**
	 * 单个查询直播回放API
	 *
	 * @author zwb
	 * @param liveRoomReplayByIdRequest 单个查询直播回放请求参数 {@link LiveRoomReplayByIdRequest}
	 * @return 直播回放详情 {@link LiveRoomReplayByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroomreplay/get-by-id")
	BaseResponse<LiveRoomReplayByIdResponse> getById(@RequestBody @Valid LiveRoomReplayByIdRequest liveRoomReplayByIdRequest);
	/**
	 * roomId查询直播回放API
	 *
	 * @author zwb
	 * @param liveRoomReplayByRoomIdRequest 列表请求参数和筛选对象 {@link LiveRoomReplayListRequest}
	 * @return 直播回放的列表信息 {@link LiveRoomReplayListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroomreplay/get-by-room-id")
	BaseResponse<LiveRoomReplayListResponse> getByRoomId(LiveRoomReplayByRoomIdRequest liveRoomReplayByRoomIdRequest);
}

