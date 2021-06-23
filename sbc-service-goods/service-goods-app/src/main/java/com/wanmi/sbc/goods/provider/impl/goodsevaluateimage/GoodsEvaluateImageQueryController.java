package com.wanmi.sbc.goods.provider.impl.goodsevaluateimage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodsevaluateimage.GoodsEvaluateImageQueryProvider;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageByIdRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageListRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImagePageRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageQueryRequest;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImageByIdResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImageListResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImagePageResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateImageVO;
import com.wanmi.sbc.goods.goodsevaluateimage.model.root.GoodsEvaluateImage;
import com.wanmi.sbc.goods.goodsevaluateimage.service.GoodsEvaluateImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商品评价图片图片查询服务接口实现</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@RestController
@Validated
public class GoodsEvaluateImageQueryController implements GoodsEvaluateImageQueryProvider {
	@Autowired
	private GoodsEvaluateImageService goodsEvaluateImageService;

	@Override
	public BaseResponse<GoodsEvaluateImagePageResponse> page(@RequestBody @Valid GoodsEvaluateImagePageRequest goodsEvaluateImagePageReq) {
		GoodsEvaluateImageQueryRequest queryReq = new GoodsEvaluateImageQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsEvaluateImagePageReq, queryReq);
		Page<GoodsEvaluateImage> goodsEvaluateImagePage = goodsEvaluateImageService.page(queryReq);
		Page<GoodsEvaluateImageVO> newPage =
				goodsEvaluateImagePage.map(entity -> goodsEvaluateImageService.wrapperVo(entity));
		MicroServicePage<GoodsEvaluateImageVO> microPage = new MicroServicePage<>(newPage, goodsEvaluateImagePageReq.getPageable());
		GoodsEvaluateImagePageResponse finalRes = new GoodsEvaluateImagePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<GoodsEvaluateImageListResponse> list(@RequestBody @Valid GoodsEvaluateImageListRequest goodsEvaluateImageListReq) {
		GoodsEvaluateImageQueryRequest queryReq = new GoodsEvaluateImageQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsEvaluateImageListReq, queryReq);
		List<GoodsEvaluateImage> goodsEvaluateImageList = goodsEvaluateImageService.list(queryReq);
		List<GoodsEvaluateImageVO> newList = goodsEvaluateImageList.stream().map(entity -> goodsEvaluateImageService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GoodsEvaluateImageListResponse(newList));
	}

	@Override
	public BaseResponse<GoodsEvaluateImageByIdResponse> getById(@RequestBody @Valid GoodsEvaluateImageByIdRequest goodsEvaluateImageByIdRequest) {
		GoodsEvaluateImage goodsEvaluateImage = goodsEvaluateImageService.getById(goodsEvaluateImageByIdRequest.getImageId());
		return BaseResponse.success(new GoodsEvaluateImageByIdResponse(goodsEvaluateImageService.wrapperVo(goodsEvaluateImage)));
	}

}

