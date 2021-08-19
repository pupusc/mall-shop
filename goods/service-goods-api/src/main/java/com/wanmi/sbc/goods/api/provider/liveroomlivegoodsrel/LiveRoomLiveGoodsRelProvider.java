package com.wanmi.sbc.goods.api.provider.liveroomlivegoodsrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelAddRequest;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelAddResponse;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelModifyRequest;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelModifyResponse;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelDelByIdRequest;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播房间和直播商品关联表保存服务Provider</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@FeignClient(value = "${application.goods.name}", contextId = "LiveRoomLiveGoodsRelProvider")
public interface LiveRoomLiveGoodsRelProvider {

	/**
	 * 新增直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelAddRequest 直播房间和直播商品关联表新增参数结构 {@link LiveRoomLiveGoodsRelAddRequest}
	 * @return 新增的直播房间和直播商品关联表信息 {@link LiveRoomLiveGoodsRelAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/add")
	BaseResponse<LiveRoomLiveGoodsRelAddResponse> add(@RequestBody @Valid LiveRoomLiveGoodsRelAddRequest liveRoomLiveGoodsRelAddRequest);

	/**
	 * 修改直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelModifyRequest 直播房间和直播商品关联表修改参数结构 {@link LiveRoomLiveGoodsRelModifyRequest}
	 * @return 修改的直播房间和直播商品关联表信息 {@link LiveRoomLiveGoodsRelModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/modify")
	BaseResponse<LiveRoomLiveGoodsRelModifyResponse> modify(@RequestBody @Valid LiveRoomLiveGoodsRelModifyRequest liveRoomLiveGoodsRelModifyRequest);

	/**
	 * 单个删除直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelDelByIdRequest 单个删除参数结构 {@link LiveRoomLiveGoodsRelDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid LiveRoomLiveGoodsRelDelByIdRequest liveRoomLiveGoodsRelDelByIdRequest);

	/**
	 * 批量删除直播房间和直播商品关联表API
	 *
	 * @author zwb
	 * @param liveRoomLiveGoodsRelDelByIdListRequest 批量删除参数结构 {@link LiveRoomLiveGoodsRelDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/liveroomlivegoodsrel/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid LiveRoomLiveGoodsRelDelByIdListRequest liveRoomLiveGoodsRelDelByIdListRequest);

}

