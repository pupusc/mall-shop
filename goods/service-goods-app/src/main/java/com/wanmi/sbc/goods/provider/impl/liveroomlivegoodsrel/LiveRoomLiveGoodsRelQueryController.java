package com.wanmi.sbc.goods.provider.impl.liveroomlivegoodsrel;

import com.wanmi.sbc.customer.bean.vo.LiveGoodsByWeChatVO;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.*;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelListByRoomIdsResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.liveroomlivegoodsrel.LiveRoomLiveGoodsRelQueryProvider;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelPageResponse;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelListResponse;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelByIdResponse;
import com.wanmi.sbc.goods.bean.vo.LiveRoomLiveGoodsRelVO;
import com.wanmi.sbc.goods.liveroomlivegoodsrel.service.LiveRoomLiveGoodsRelService;
import com.wanmi.sbc.goods.liveroomlivegoodsrel.model.root.LiveRoomLiveGoodsRel;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>直播房间和直播商品关联表查询服务接口实现</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@RestController
@Validated
public class LiveRoomLiveGoodsRelQueryController implements LiveRoomLiveGoodsRelQueryProvider {
	@Autowired
	private LiveRoomLiveGoodsRelService liveRoomLiveGoodsRelService;

	@Override
	public BaseResponse<LiveRoomLiveGoodsRelPageResponse> page(@RequestBody @Valid LiveRoomLiveGoodsRelPageRequest liveRoomLiveGoodsRelPageReq) {
		LiveRoomLiveGoodsRelQueryRequest queryReq = KsBeanUtil.convert(liveRoomLiveGoodsRelPageReq, LiveRoomLiveGoodsRelQueryRequest.class);
		Page<LiveRoomLiveGoodsRel> liveRoomLiveGoodsRelPage = liveRoomLiveGoodsRelService.page(queryReq);
		Page<LiveRoomLiveGoodsRelVO> newPage = liveRoomLiveGoodsRelPage.map(entity -> liveRoomLiveGoodsRelService.wrapperVo(entity));
		MicroServicePage<LiveRoomLiveGoodsRelVO> microPage = new MicroServicePage<>(newPage, liveRoomLiveGoodsRelPageReq.getPageable());
		LiveRoomLiveGoodsRelPageResponse finalRes = new LiveRoomLiveGoodsRelPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<LiveRoomLiveGoodsRelListResponse> list(@RequestBody @Valid LiveRoomLiveGoodsRelListRequest liveRoomLiveGoodsRelListReq) {
		LiveRoomLiveGoodsRelQueryRequest queryReq = KsBeanUtil.convert(liveRoomLiveGoodsRelListReq, LiveRoomLiveGoodsRelQueryRequest.class);
		List<LiveRoomLiveGoodsRel> liveRoomLiveGoodsRelList = liveRoomLiveGoodsRelService.list(queryReq);
		List<LiveRoomLiveGoodsRelVO> newList = liveRoomLiveGoodsRelList.stream().map(entity -> liveRoomLiveGoodsRelService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new LiveRoomLiveGoodsRelListResponse(newList));
	}

	@Override
	public BaseResponse<LiveRoomLiveGoodsRelByIdResponse> getById(@RequestBody @Valid LiveRoomLiveGoodsRelByIdRequest liveRoomLiveGoodsRelByIdRequest) {
		LiveRoomLiveGoodsRel liveRoomLiveGoodsRel =
		liveRoomLiveGoodsRelService.getOne(liveRoomLiveGoodsRelByIdRequest.getId());
		return BaseResponse.success(new LiveRoomLiveGoodsRelByIdResponse(liveRoomLiveGoodsRelService.wrapperVo(liveRoomLiveGoodsRel)));
	}

	@Override
	public BaseResponse<LiveRoomLiveGoodsRelListResponse> getByRoomId(@Valid LiveRoomLiveGoodsRelByRoomIdRequest liveRoomLiveGoodsRelByRoomIdRequest) {
		List<LiveRoomLiveGoodsRel> list = liveRoomLiveGoodsRelService.getByRoomId(liveRoomLiveGoodsRelByRoomIdRequest.getRoomId());
		List<LiveRoomLiveGoodsRelVO> newList = list.stream().map(entity -> liveRoomLiveGoodsRelService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new LiveRoomLiveGoodsRelListResponse(newList));
	}

	@Override
	public BaseResponse<LiveRoomLiveGoodsRelListByRoomIdsResponse> listByRoomId(@Valid LiveRoomLiveGoodsRelListByRoomIdRequest request) {
		Map<Long, List<LiveGoodsByWeChatVO>> result =  liveRoomLiveGoodsRelService.listByLiveGoods(request);
		return BaseResponse.success(LiveRoomLiveGoodsRelListByRoomIdsResponse.builder().result(result).build());
	}

}

