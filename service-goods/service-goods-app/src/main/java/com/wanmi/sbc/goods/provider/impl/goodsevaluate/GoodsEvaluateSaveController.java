package com.wanmi.sbc.goods.provider.impl.goodsevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodsevaluate.GoodsEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.request.goodsevaluate.*;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateAddResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateAnswerResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateModifyResponse;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import com.wanmi.sbc.goods.goodsevaluate.service.GoodsEvaluateService;
import com.wanmi.sbc.goods.goodsevaluateimage.service.GoodsEvaluateImageService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>商品评价保存服务接口实现</p>
 * @author liutao
 * @date 2019-02-25 15:14:16
 */
@RestController
@Validated
public class GoodsEvaluateSaveController implements GoodsEvaluateSaveProvider {
	@Autowired
	private GoodsEvaluateService goodsEvaluateService;

	@Autowired
	private GoodsEvaluateImageService goodsEvaluateImageService;

	@Autowired
	private GoodsService goodsService;

	@Override
	public BaseResponse<GoodsEvaluateAddResponse> add(@RequestBody @Valid GoodsEvaluateAddRequest goodsEvaluateAddRequest) {
		GoodsEvaluate goodsEvaluate = new GoodsEvaluate();
		KsBeanUtil.copyPropertiesThird(goodsEvaluateAddRequest, goodsEvaluate);
		return BaseResponse.success(new GoodsEvaluateAddResponse(
				goodsEvaluateService.wrapperVo(goodsEvaluateService.add(goodsEvaluate))));
	}

	@Override
	public BaseResponse addBookFriendEvaluate(@RequestBody BookFriendEvaluateAddRequest bookFriendEvaluateAddRequest) {
		goodsEvaluateService.addBookFriendEvaluate(bookFriendEvaluateAddRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse editBookFriendEvaluate(@RequestBody BookFriendEvaluateEditRequest bookFriendEvaluateEditRequest) {
		goodsEvaluateService.editBookFriendEvaluate(bookFriendEvaluateEditRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse addList(@RequestBody GoodsEvaluateAddListRequest goodsEvaluateAddListRequest) {
		List<GoodsEvaluate> goodsEvaluateList = new ArrayList<>();
		goodsEvaluateAddListRequest.getGoodsEvaluateAddList().forEach(goodsEvaluateAddRequest -> {
			GoodsEvaluate goodsEvaluate = new GoodsEvaluate();
			KsBeanUtil.copyPropertiesThird(goodsEvaluateAddRequest, goodsEvaluate);
			goodsEvaluateList.add(goodsEvaluate);
		});
		goodsEvaluateService.addList(goodsEvaluateList);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse<GoodsEvaluateModifyResponse> modify(@RequestBody @Valid GoodsEvaluateModifyRequest goodsEvaluateModifyRequest) {
		GoodsEvaluate goodsEvaluate = new GoodsEvaluate();
		KsBeanUtil.copyPropertiesThird(goodsEvaluateModifyRequest, goodsEvaluate);
		return BaseResponse.success(new GoodsEvaluateModifyResponse(
				goodsEvaluateService.wrapperVo(goodsEvaluateService.modify(goodsEvaluate))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid GoodsEvaluateDelByIdRequest goodsEvaluateDelByIdRequest) {
		goodsEvaluateService.deleteById(goodsEvaluateDelByIdRequest.getEvaluateId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid GoodsEvaluateDelByIdListRequest goodsEvaluateDelByIdListRequest) {
		goodsEvaluateService.deleteByIdList(goodsEvaluateDelByIdListRequest.getEvaluateIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse<GoodsEvaluateAnswerResponse> answer(@RequestBody @Valid GoodsEvaluateAnswerRequest request) {
		return BaseResponse.success(new GoodsEvaluateAnswerResponse(
				goodsEvaluateService.wrapperVo(goodsEvaluateService.answer(request))));
	}

}

