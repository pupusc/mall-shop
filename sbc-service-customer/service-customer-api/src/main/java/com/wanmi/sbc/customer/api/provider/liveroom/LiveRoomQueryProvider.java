package com.wanmi.sbc.customer.api.provider.liveroom;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomListByWeChatRequest;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomPageRequest;
import com.wanmi.sbc.customer.api.response.liveroom.*;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomListRequest;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomByIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播间查询服务Provider</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@FeignClient(value = "${application.customer.name}", contextId = "LiveRoomQueryProvider")
public interface LiveRoomQueryProvider {

	/**
	 * 分页查询直播间API
	 *
	 * @author zwb
	 * @param liveRoomPageReq 分页请求参数和筛选对象 {@link LiveRoomPageRequest}
	 * @return 直播间分页列表信息 {@link LiveRoomPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/page")
	BaseResponse<LiveRoomPageResponse> page(@RequestBody @Valid LiveRoomPageRequest liveRoomPageReq);

	/**
	 * 列表查询直播间API
	 *
	 * @author zwb
	 * @param liveRoomListReq 列表请求参数和筛选对象 {@link LiveRoomListRequest}
	 * @return 直播间的列表信息 {@link LiveRoomListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/list")
	BaseResponse<LiveRoomListResponse> list(@RequestBody @Valid LiveRoomListRequest liveRoomListReq);

	/**
	 * 单个查询直播间API
	 *
	 * @author zwb
	 * @param liveRoomByIdRequest 单个查询直播间请求参数 {@link LiveRoomByIdRequest}
	 * @return 直播间详情 {@link LiveRoomByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/get-by-id")
	BaseResponse<LiveRoomByIdResponse> getById(@RequestBody @Valid LiveRoomByIdRequest liveRoomByIdRequest);


	/**
	 * 分页查询直播间API
	 *
	 * @author zwb
	 * @param liveRoomPageReq 分页请求参数和筛选对象 {@link LiveRoomPageRequest}
	 * @return 直播间分页列表信息 {@link LiveRoomPageAllResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/liveroom/page-new")
	BaseResponse<LiveRoomPageAllResponse> pageNew(@RequestBody @Valid LiveRoomPageRequest liveRoomPageReq);

}

