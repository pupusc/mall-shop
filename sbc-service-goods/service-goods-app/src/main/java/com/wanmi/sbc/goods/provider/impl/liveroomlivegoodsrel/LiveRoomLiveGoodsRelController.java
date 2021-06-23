package com.wanmi.sbc.goods.provider.impl.liveroomlivegoodsrel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.provider.liveroomlivegoodsrel.LiveRoomLiveGoodsRelProvider;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelAddRequest;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelAddResponse;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelModifyRequest;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelModifyResponse;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelDelByIdRequest;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelDelByIdListRequest;
import com.wanmi.sbc.goods.liveroomlivegoodsrel.service.LiveRoomLiveGoodsRelService;
import com.wanmi.sbc.goods.liveroomlivegoodsrel.model.root.LiveRoomLiveGoodsRel;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>直播房间和直播商品关联表保存服务接口实现</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@RestController
@Validated
public class LiveRoomLiveGoodsRelController implements LiveRoomLiveGoodsRelProvider {
	@Autowired
	private LiveRoomLiveGoodsRelService liveRoomLiveGoodsRelService;

	@Override
	public BaseResponse<LiveRoomLiveGoodsRelAddResponse> add(@RequestBody @Valid LiveRoomLiveGoodsRelAddRequest liveRoomLiveGoodsRelAddRequest) {
		LiveRoomLiveGoodsRel liveRoomLiveGoodsRel = KsBeanUtil.convert(liveRoomLiveGoodsRelAddRequest, LiveRoomLiveGoodsRel.class);
		return BaseResponse.success(new LiveRoomLiveGoodsRelAddResponse(
				liveRoomLiveGoodsRelService.wrapperVo(liveRoomLiveGoodsRelService.add(liveRoomLiveGoodsRel))));
	}

	@Override
	public BaseResponse<LiveRoomLiveGoodsRelModifyResponse> modify(@RequestBody @Valid LiveRoomLiveGoodsRelModifyRequest liveRoomLiveGoodsRelModifyRequest) {
		LiveRoomLiveGoodsRel liveRoomLiveGoodsRel = KsBeanUtil.convert(liveRoomLiveGoodsRelModifyRequest, LiveRoomLiveGoodsRel.class);
		return BaseResponse.success(new LiveRoomLiveGoodsRelModifyResponse(
				liveRoomLiveGoodsRelService.wrapperVo(liveRoomLiveGoodsRelService.modify(liveRoomLiveGoodsRel))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid LiveRoomLiveGoodsRelDelByIdRequest liveRoomLiveGoodsRelDelByIdRequest) {
		LiveRoomLiveGoodsRel liveRoomLiveGoodsRel = KsBeanUtil.convert(liveRoomLiveGoodsRelDelByIdRequest, LiveRoomLiveGoodsRel.class);
		liveRoomLiveGoodsRel.setDelFlag(DeleteFlag.YES);
		liveRoomLiveGoodsRelService.deleteById(liveRoomLiveGoodsRel);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid LiveRoomLiveGoodsRelDelByIdListRequest liveRoomLiveGoodsRelDelByIdListRequest) {
		List<LiveRoomLiveGoodsRel> liveRoomLiveGoodsRelList = liveRoomLiveGoodsRelDelByIdListRequest.getIdList().stream()
			.map(Id -> {
				LiveRoomLiveGoodsRel liveRoomLiveGoodsRel = KsBeanUtil.convert(Id, LiveRoomLiveGoodsRel.class);
				liveRoomLiveGoodsRel.setDelFlag(DeleteFlag.YES);
				return liveRoomLiveGoodsRel;
			}).collect(Collectors.toList());
		liveRoomLiveGoodsRelService.deleteByIdList(liveRoomLiveGoodsRelList);
		return BaseResponse.SUCCESSFUL();
	}

}

