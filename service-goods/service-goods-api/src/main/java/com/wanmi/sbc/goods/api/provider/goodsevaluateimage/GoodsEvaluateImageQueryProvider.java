package com.wanmi.sbc.goods.api.provider.goodsevaluateimage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImagePageRequest;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImagePageResponse;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageListRequest;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImageListResponse;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageByIdRequest;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImageByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>商品评价图片查询服务Provider</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsEvaluateImageQueryProvider")
public interface GoodsEvaluateImageQueryProvider {

	/**
	 * 分页查询商品评价图片API
	 *
	 * @author liutao
	 * @param goodsEvaluateImagePageReq 分页请求参数和筛选对象 {@link GoodsEvaluateImagePageRequest}
	 * @return 商品评价图片分页列表信息 {@link GoodsEvaluateImagePageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/page")
	BaseResponse<GoodsEvaluateImagePageResponse> page(@RequestBody @Valid GoodsEvaluateImagePageRequest goodsEvaluateImagePageReq);

	/**
	 * 列表查询商品评价图片API
	 *
	 * @author liutao
	 * @param goodsEvaluateImageListReq 列表请求参数和筛选对象 {@link GoodsEvaluateImageListRequest}
	 * @return 商品评价图片的列表信息 {@link GoodsEvaluateImageListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/list")
	BaseResponse<GoodsEvaluateImageListResponse> list(@RequestBody @Valid GoodsEvaluateImageListRequest goodsEvaluateImageListReq);

	/**
	 * 单个查询商品评价图片API
	 *
	 * @author liutao
	 * @param goodsEvaluateImageByIdRequest 单个查询商品评价图片请求参数 {@link GoodsEvaluateImageByIdRequest}
	 * @return 商品评价图片详情 {@link GoodsEvaluateImageByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/get-by-id")
	BaseResponse<GoodsEvaluateImageByIdResponse> getById(@RequestBody @Valid GoodsEvaluateImageByIdRequest goodsEvaluateImageByIdRequest);

}

