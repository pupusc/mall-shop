package com.wanmi.sbc.goods.api.provider.goodsevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodsevaluate.*;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateAddResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateAnswerResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品评价保存服务Provider</p>
 * @author liutao
 * @date 2019-02-25 15:17:42
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsEvaluateSaveProvider")
public interface GoodsEvaluateSaveProvider {

	/**
	 * 新增商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluateAddRequest 商品评价新增参数结构 {@link GoodsEvaluateAddRequest}
	 * @return 新增的商品评价信息 {@link GoodsEvaluateAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/add")
	BaseResponse<GoodsEvaluateAddResponse> add(@RequestBody @Valid GoodsEvaluateAddRequest goodsEvaluateAddRequest);

	/**
	 * 新增书友说评价API
	 */
	@PostMapping("/goods/${application.goods.version}/bookFriendEvaluate/add")
	BaseResponse addBookFriendEvaluate(@RequestBody @Valid BookFriendEvaluateAddRequest bookFriendEvaluateAddRequest);

	/**
	 * 编辑书友说评价API
	 */
	@PostMapping("/goods/${application.goods.version}/bookFriendEvaluate/edit")
    BaseResponse editBookFriendEvaluate(@RequestBody BookFriendEvaluateEditRequest bookFriendEvaluateEditRequest);

    /**
	 * 新增商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluateAddListRequest 商品批量评价新增参数结构 {@link GoodsEvaluateAddRequest}
	 * @return 批量新增的商品评价信息 {@link GoodsEvaluateAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/add-list")
	BaseResponse addList(@RequestBody GoodsEvaluateAddListRequest goodsEvaluateAddListRequest);

	/**
	 * 修改商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluateModifyRequest 商品评价修改参数结构 {@link GoodsEvaluateModifyRequest}
	 * @return 修改的商品评价信息 {@link GoodsEvaluateModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/modify")
	BaseResponse<GoodsEvaluateModifyResponse> modify(@RequestBody @Valid GoodsEvaluateModifyRequest goodsEvaluateModifyRequest);

	/**
	 * 单个删除商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluateDelByIdRequest 单个删除参数结构 {@link GoodsEvaluateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GoodsEvaluateDelByIdRequest goodsEvaluateDelByIdRequest);

	/**
	 * 批量删除商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluateDelByIdListRequest 批量删除参数结构 {@link GoodsEvaluateDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid GoodsEvaluateDelByIdListRequest goodsEvaluateDelByIdListRequest);

	/**
	 * 修改商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluateModifyRequest 商品评价修改参数结构 {@link GoodsEvaluateAnswerRequest}
	 * @return 修改的商品评价信息 {@link GoodsEvaluateAnswerResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/answer")
	BaseResponse<GoodsEvaluateAnswerResponse> answer(@RequestBody @Valid GoodsEvaluateAnswerRequest goodsEvaluateModifyRequest);

}

