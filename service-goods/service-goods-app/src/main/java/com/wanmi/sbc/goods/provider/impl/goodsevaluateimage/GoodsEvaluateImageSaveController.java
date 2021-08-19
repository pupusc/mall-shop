package com.wanmi.sbc.goods.provider.impl.goodsevaluateimage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodsevaluateimage.GoodsEvaluateImageSaveProvider;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.*;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImageAddResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImageModifyResponse;
import com.wanmi.sbc.goods.goodsevaluateimage.model.root.GoodsEvaluateImage;
import com.wanmi.sbc.goods.goodsevaluateimage.service.GoodsEvaluateImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>商品评价图片保存服务接口实现</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@RestController
@Validated
public class GoodsEvaluateImageSaveController implements GoodsEvaluateImageSaveProvider {
	@Autowired
	private GoodsEvaluateImageService goodsEvaluateImageService;

	@Override
	public BaseResponse<GoodsEvaluateImageAddResponse> add(@RequestBody @Valid GoodsEvaluateImageAddRequest goodsEvaluateImageAddRequest) {
		GoodsEvaluateImage goodsEvaluateImage = new GoodsEvaluateImage();
		KsBeanUtil.copyPropertiesThird(goodsEvaluateImageAddRequest, goodsEvaluateImage);
		return BaseResponse.success(new GoodsEvaluateImageAddResponse(
				goodsEvaluateImageService.wrapperVo(goodsEvaluateImageService.add(goodsEvaluateImage))));
	}

	@Override
	public BaseResponse<GoodsEvaluateImageModifyResponse> modify(@RequestBody @Valid GoodsEvaluateImageModifyRequest goodsEvaluateImageModifyRequest) {
		GoodsEvaluateImage goodsEvaluateImage = new GoodsEvaluateImage();
		KsBeanUtil.copyPropertiesThird(goodsEvaluateImageModifyRequest, goodsEvaluateImage);
		return BaseResponse.success(new GoodsEvaluateImageModifyResponse(
				goodsEvaluateImageService.wrapperVo(goodsEvaluateImageService.modify(goodsEvaluateImage))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid GoodsEvaluateImageDelByIdRequest goodsEvaluateImageDelByIdRequest) {
		goodsEvaluateImageService.deleteById(goodsEvaluateImageDelByIdRequest.getImageId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid GoodsEvaluateImageDelByIdListRequest goodsEvaluateImageDelByIdListRequest) {
		goodsEvaluateImageService.deleteByIdList(goodsEvaluateImageDelByIdListRequest.getImageIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByEvaluateId(@RequestBody @Valid GoodsEvaluateImageDelByEvaluateIdRequest
													   goodsEvaluateImageDelByEvaluateIdRequest) {
		goodsEvaluateImageService.deleteByEvaluateId(goodsEvaluateImageDelByEvaluateIdRequest);
		return BaseResponse.SUCCESSFUL();
	}

	/**
	 * @param queryRequest {@link EvaluateImgUpdateIsShowReq}
	 * @Description: 商品ID更新晒单是否显示
	 * @Author: Bob
	 * @Date: 2019-04-24 16:58
	 */
	@Override
	public BaseResponse<Integer> updateIsShowBygoodsId(@RequestBody @Valid EvaluateImgUpdateIsShowReq queryRequest) {
		return BaseResponse.success(goodsEvaluateImageService.updateIsShowByEvaluateId(queryRequest));
	}

}

