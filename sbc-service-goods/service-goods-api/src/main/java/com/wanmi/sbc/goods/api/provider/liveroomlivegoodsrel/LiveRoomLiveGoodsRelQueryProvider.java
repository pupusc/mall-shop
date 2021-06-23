package com.wanmi.sbc.goods.api.provider.liveroomlivegoodsrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.*;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelListByRoomIdsResponse;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelPageResponse;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelListResponse;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播房间和直播商品关联表查询服务Provider</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@FeignClient(value = "${application.goods.name}", contextId = "LiveRoomLiveGoodsRelQueryProvider")
public interface LiveRoomLiveGoodsRelQueryProvider {

	/**
	 * 分页查询直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelPageReq 分页请求参数和筛选对象 {@link LiveRoomLiveGoodsRelPageRequest}
	 * @return 直播房间和直播商品关联表分页列表信息 {@link LiveRoomLiveGoodsRelPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/page")
	BaseResponse<LiveRoomLiveGoodsRelPageResponse> page(@RequestBody @Valid LiveRoomLiveGoodsRelPageRequest liveRoomLiveGoodsRelPageReq);

	/**
	 * 列表查询直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelListReq 列表请求参数和筛选对象 {@link LiveRoomLiveGoodsRelListRequest}
	 * @return 直播房间和直播商品关联表的列表信息 {@link LiveRoomLiveGoodsRelListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/list")
	BaseResponse<LiveRoomLiveGoodsRelListResponse> list(@RequestBody @Valid LiveRoomLiveGoodsRelListRequest liveRoomLiveGoodsRelListReq);

	/**
	 * 单个查询直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelByIdRequest 单个查询直播房间和直播商品关联表请求参数 {@link LiveRoomLiveGoodsRelByIdRequest}
	 * @return 直播房间和直播商品关联表详情 {@link LiveRoomLiveGoodsRelByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/get-by-id")
	BaseResponse<LiveRoomLiveGoodsRelByIdResponse> getById(@RequestBody @Valid LiveRoomLiveGoodsRelByIdRequest liveRoomLiveGoodsRelByIdRequest);
	/**
	 * 单个查询直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelByRoomIdRequest 单个查询直播房间和直播商品关联表请求参数 {@link LiveRoomLiveGoodsRelByIdRequest}
	 * @return 直播房间和直播商品关联表详情 {@link LiveRoomLiveGoodsRelByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/get-by-room-id")
	BaseResponse<LiveRoomLiveGoodsRelListResponse> getByRoomId(@RequestBody @Valid LiveRoomLiveGoodsRelByRoomIdRequest liveRoomLiveGoodsRelByRoomIdRequest);

	/**
	 * 批量查询直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelByRoomIdRequest 单个查询直播房间和直播商品关联表请求参数 {@link LiveRoomLiveGoodsRelListByRoomIdRequest}
	 * @return 直播房间和直播商品关联表详情 {@link LiveRoomLiveGoodsRelByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/list-by-room-ids")
	BaseResponse<LiveRoomLiveGoodsRelListByRoomIdsResponse> listByRoomId(@RequestBody @Valid LiveRoomLiveGoodsRelListByRoomIdRequest liveRoomLiveGoodsRelByRoomIdRequest);
}

