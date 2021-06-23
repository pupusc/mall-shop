package com.wanmi.sbc.goods.provider.impl.goodstobeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateByIdRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateListRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluatePageRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateByIdResponse;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateListResponse;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluatePageResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsTobeEvaluateVO;
import com.wanmi.sbc.goods.goodstobeevaluate.model.root.GoodsTobeEvaluate;
import com.wanmi.sbc.goods.goodstobeevaluate.service.GoodsTobeEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>订单商品待评价查询服务接口实现</p>
 * @author lzw
 * @date 2019-03-20 11:21:41
 */
@RestController
@Validated
public class GoodsTobeEvaluateQueryController implements GoodsTobeEvaluateQueryProvider {
	@Autowired
	private GoodsTobeEvaluateService goodsTobeEvaluateService;

	@Override
	public BaseResponse<GoodsTobeEvaluatePageResponse> page(@RequestBody @Valid GoodsTobeEvaluatePageRequest goodsTobeEvaluatePageReq) {
		GoodsTobeEvaluateQueryRequest queryReq = new GoodsTobeEvaluateQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsTobeEvaluatePageReq, queryReq);
		Page<GoodsTobeEvaluate> goodsTobeEvaluatePage = goodsTobeEvaluateService.page(queryReq);
		Page<GoodsTobeEvaluateVO> newPage = goodsTobeEvaluatePage.map(entity -> goodsTobeEvaluateService.wrapperVo(entity));
		MicroServicePage<GoodsTobeEvaluateVO> microPage = new MicroServicePage<>(newPage, goodsTobeEvaluatePageReq.getPageable());
		GoodsTobeEvaluatePageResponse finalRes = new GoodsTobeEvaluatePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<GoodsTobeEvaluateListResponse> list(@RequestBody @Valid GoodsTobeEvaluateListRequest goodsTobeEvaluateListReq) {
		GoodsTobeEvaluateQueryRequest queryReq = new GoodsTobeEvaluateQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsTobeEvaluateListReq, queryReq);
		List<GoodsTobeEvaluate> goodsTobeEvaluateList = goodsTobeEvaluateService.list(queryReq);
		List<GoodsTobeEvaluateVO> newList = goodsTobeEvaluateList.stream().map(entity -> goodsTobeEvaluateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GoodsTobeEvaluateListResponse(newList));
	}

	@Override
	public BaseResponse<GoodsTobeEvaluateByIdResponse> getById(@RequestBody @Valid GoodsTobeEvaluateByIdRequest goodsTobeEvaluateByIdRequest) {
		GoodsTobeEvaluate goodsTobeEvaluate = goodsTobeEvaluateService.getById(goodsTobeEvaluateByIdRequest.getId());
		return BaseResponse.success(new GoodsTobeEvaluateByIdResponse(goodsTobeEvaluateService.wrapperVo(goodsTobeEvaluate)));
	}

	@Override
	public BaseResponse<Long> getGoodsTobeEvaluateNum(@RequestBody GoodsTobeEvaluateQueryRequest queryReq){
		return BaseResponse.success(goodsTobeEvaluateService.getGoodsTobeEvaluateNum(queryReq));
	}

	@Override
	public BaseResponse autoGoodsEvaluate() {
		goodsTobeEvaluateService.autoGoodsEvaluate();
		return BaseResponse.SUCCESSFUL();
	}

}

