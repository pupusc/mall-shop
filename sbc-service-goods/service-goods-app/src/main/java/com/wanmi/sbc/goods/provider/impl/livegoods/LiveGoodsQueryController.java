package com.wanmi.sbc.goods.provider.impl.livegoods;

import com.wanmi.sbc.goods.api.request.livegoods.*;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsPageNewResponse;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import com.wanmi.sbc.goods.livegoods.model.root.LiveGoods;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsQueryProvider;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsPageResponse;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsListResponse;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsByIdResponse;
import com.wanmi.sbc.goods.livegoods.service.LiveGoodsService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>直播商品查询服务接口实现</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@RestController
@Validated
public class LiveGoodsQueryController implements LiveGoodsQueryProvider {
	@Autowired
	private LiveGoodsService liveGoodsService;

	@Override
	public BaseResponse<LiveGoodsPageResponse> page(@RequestBody @Valid LiveGoodsPageRequest liveGoodsPageReq) {
		LiveGoodsQueryRequest queryReq = KsBeanUtil.convert(liveGoodsPageReq, LiveGoodsQueryRequest.class);
		Page<LiveGoods> liveGoodsPage = liveGoodsService.page(queryReq);
		Page<LiveGoodsVO> newPage = liveGoodsPage.map(entity -> liveGoodsService.wrapperVo(entity));
		MicroServicePage<LiveGoodsVO> microPage = new MicroServicePage<>(newPage, liveGoodsPageReq.getPageable());
		LiveGoodsPageResponse finalRes = new LiveGoodsPageResponse();
		finalRes.setLiveGoodsVOPage(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<LiveGoodsListResponse> list(@RequestBody @Valid LiveGoodsListRequest liveGoodsListReq) {
		LiveGoodsQueryRequest queryReq = KsBeanUtil.convert(liveGoodsListReq, LiveGoodsQueryRequest.class);
		List<LiveGoods> liveGoodsList = liveGoodsService.list(queryReq);
		List<LiveGoodsVO> newList = liveGoodsList.stream().map(entity -> liveGoodsService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new LiveGoodsListResponse(newList));
	}

	@Override
	public BaseResponse<LiveGoodsByIdResponse> getById(@RequestBody @Valid LiveGoodsByIdRequest liveGoodsByIdRequest) {
		LiveGoods liveGoods =
				liveGoodsService.getOne(liveGoodsByIdRequest.getGoodsId());
		LiveGoodsVO liveGoodsVO = liveGoodsService.wrapperVo(liveGoods);
		LiveGoodsByIdResponse response = new LiveGoodsByIdResponse();
		response.setLiveGoodsVO(liveGoodsVO);
		return BaseResponse.success(response);
	}

	@Override
	public BaseResponse<LiveGoodsPageNewResponse> pageNew(@RequestBody @Valid LiveGoodsQueryRequest request) {
		LiveGoodsPageNewResponse pageResponse = liveGoodsService.pageNew(request);
		return BaseResponse.success(pageResponse);
	}

}

