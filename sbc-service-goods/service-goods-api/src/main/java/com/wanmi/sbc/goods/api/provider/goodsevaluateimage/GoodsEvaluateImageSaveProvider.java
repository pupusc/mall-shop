package com.wanmi.sbc.goods.api.provider.goodsevaluateimage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.*;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImageAddResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImageModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品评价图片保存服务Provider</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsEvaluateImageSaveProvider")
public interface GoodsEvaluateImageSaveProvider {

	/**
	 * 新增商品评价图片API
	 *
	 * @author liutao
	 * @param goodsEvaluateImageAddRequest 商品评价图片新增参数结构 {@link GoodsEvaluateImageAddRequest}
	 * @return 新增的商品评价图片信息 {@link GoodsEvaluateImageAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/add")
	BaseResponse<GoodsEvaluateImageAddResponse> add(@RequestBody @Valid GoodsEvaluateImageAddRequest goodsEvaluateImageAddRequest);

	/**
	 * 修改商品评价图片API
	 *
	 * @author liutao
	 * @param goodsEvaluateImageModifyRequest 商品评价图片修改参数结构 {@link GoodsEvaluateImageModifyRequest}
	 * @return 修改的商品评价图片信息 {@link GoodsEvaluateImageModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/modify")
	BaseResponse<GoodsEvaluateImageModifyResponse> modify(@RequestBody @Valid GoodsEvaluateImageModifyRequest goodsEvaluateImageModifyRequest);

	/**
	 * 单个删除商品评价图片API
	 *
	 * @author liutao
	 * @param goodsEvaluateImageDelByIdRequest 单个删除参数结构 {@link GoodsEvaluateImageDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GoodsEvaluateImageDelByIdRequest goodsEvaluateImageDelByIdRequest);

	/**
	 * 批量删除商品评价图片API
	 *
	 * @author liutao
	 * @param goodsEvaluateImageDelByIdListRequest 批量删除参数结构 {@link GoodsEvaluateImageDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid GoodsEvaluateImageDelByIdListRequest goodsEvaluateImageDelByIdListRequest);

	/**
	 * 根据评价id删除晒单图片
	 * @param goodsEvaluateImageDelByEvaluateIdRequest
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/delete-by-evaluateId")
	BaseResponse deleteByEvaluateId(@RequestBody @Valid GoodsEvaluateImageDelByEvaluateIdRequest
										  goodsEvaluateImageDelByEvaluateIdRequest);


	/**
	 * @Description: 商品ID更新晒单是否显示
	 * @param queryRequest {@link EvaluateImgUpdateIsShowReq}
	 * @Author: Bob
	 * @Date: 2019-04-24 16:58
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateimage/update-isShow-by-goodsId")
	BaseResponse updateIsShowBygoodsId(@RequestBody @Valid EvaluateImgUpdateIsShowReq queryRequest);
}

