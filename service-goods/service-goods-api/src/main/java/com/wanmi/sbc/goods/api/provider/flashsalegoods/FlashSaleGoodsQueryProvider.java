package com.wanmi.sbc.goods.api.provider.flashsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.flashsalegoods.*;
import com.wanmi.sbc.goods.api.response.flashsalegoods.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>抢购商品表查询服务Provider</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@FeignClient(value = "${application.goods.name}", contextId = "FlashSaleGoodsQueryProvider")
public interface FlashSaleGoodsQueryProvider {

	/**
	 * 分页查询抢购商品表API
	 *
	 * @author bob
	 * @param flashSaleGoodsPageReq 分页请求参数和筛选对象 {@link FlashSaleGoodsPageRequest}
	 * @return 抢购商品表分页列表信息 {@link FlashSaleGoodsPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/page")
	BaseResponse<FlashSaleGoodsPageResponse> page(@RequestBody @Valid FlashSaleGoodsPageRequest flashSaleGoodsPageReq);

	/**
	 * 列表查询抢购商品表API
	 *
	 * @author bob
	 * @param flashSaleGoodsListReq 列表请求参数和筛选对象 {@link FlashSaleGoodsListRequest}
	 * @return 抢购商品表的列表信息 {@link FlashSaleGoodsListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/list")
	BaseResponse<FlashSaleGoodsListResponse> list(@RequestBody @Valid FlashSaleGoodsListRequest flashSaleGoodsListReq);

	/**
	 * 单个查询抢购商品表API
	 *
	 * @author bob
	 * @param flashSaleGoodsByIdRequest 单个查询抢购商品表请求参数 {@link FlashSaleGoodsByIdRequest}
	 * @return 抢购商品表详情 {@link FlashSaleGoodsByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/get-by-id")
	BaseResponse<FlashSaleGoodsByIdResponse> getById(@RequestBody @Valid FlashSaleGoodsByIdRequest flashSaleGoodsByIdRequest);

	/**
	 * 商品是否正在抢购API
	 *
	 * @author bob
	 * @param isInProgressReq 单个查询抢购商品表请求参数 {@link IsInProgressReq}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/isInProgress")
	BaseResponse<IsInProgressResp> isInProgress(@RequestBody @Valid IsInProgressReq isInProgressReq);

	/**
	 * 获取参与商家数量API
	 *
	 * @author yxz
	 * @return 参与商家数量 {@link FlashSaleGoodsStoreCountResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/storeCount")
	BaseResponse<FlashSaleGoodsStoreCountResponse> storeCount();

	/**
	 * 是否有未结束活动关联商品
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/isFlashSale")
	BaseResponse<IsFlashSaleResponse> isFlashSale(@RequestBody @Valid IsFlashSaleRequest isFlashSaleRequest);
}

