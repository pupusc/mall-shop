package com.wanmi.sbc.goods.api.provider.groupongoodsinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.*;
import com.wanmi.sbc.goods.api.response.groupongoodsinfo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>拼团活动商品信息表查询服务Provider</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@FeignClient(value = "${application.goods.name}", contextId = "GrouponGoodsInfoQueryProvider")
public interface GrouponGoodsInfoQueryProvider {

	/**
	 * 分页查询拼团活动商品信息表API
	 *
	 * @author groupon
	 * @param grouponGoodsInfoPageReq 分页请求参数和筛选对象 {@link GrouponGoodsInfoPageRequest}
	 * @return 拼团活动商品信息表分页列表信息 {@link GrouponGoodsInfoPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/page")
	BaseResponse<GrouponGoodsInfoPageResponse> page(@RequestBody @Valid GrouponGoodsInfoPageRequest
                                                            grouponGoodsInfoPageReq);

	/**
	 * 列表查询拼团活动商品信息表API
	 *
	 * @author groupon
	 * @param grouponGoodsInfoListReq 列表请求参数和筛选对象 {@link GrouponGoodsInfoListRequest}
	 * @return 拼团活动商品信息表的列表信息 {@link GrouponGoodsInfoListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/list")
	BaseResponse<GrouponGoodsInfoListResponse> list(@RequestBody @Valid GrouponGoodsInfoListRequest grouponGoodsInfoListReq);

	/**
	 * 单个查询拼团活动商品信息表API
	 *
	 * @author groupon
	 * @param grouponGoodsInfoByIdRequest 单个查询拼团活动商品信息表请求参数 {@link GrouponGoodsInfoByIdRequest}
	 * @return 拼团活动商品信息表详情 {@link GrouponGoodsInfoByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/get-by-id")
	BaseResponse<GrouponGoodsInfoByIdResponse> getById(@RequestBody @Valid GrouponGoodsInfoByIdRequest grouponGoodsInfoByIdRequest);

	/**
	 * 根据拼团活动ID、SPU编号查询最低拼团价格的拼团信息（批量查询）
	 *
	 * @author groupon
	 * @param request 拼团活动ID、SPU编号集合信息 {@link GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest}
	 * @return 拼团活动ID、SPU编号、最低拼团价格集合信息 {@link GrouponGoodsInfoByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/batch-by-activity-id-and-goods-id")
	BaseResponse<GrouponGoodsInfoBatchByActivityIdAndGoodsIdResponse> batchByActivityIdAndGoodsId(@RequestBody @Valid GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest request);


	/**
	 * 分页查询拼团活动商品SPU信息API
	 *
	 * @author groupon
	 * @param grouponGoodsPageRequest 分页请求参数和筛选对象 {@link GrouponGoodsPageRequest}
	 * @return 分页查询拼团活动商品SPU信息 {@link GrouponGoodsPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/spu-page")
	BaseResponse<GrouponGoodsPageResponse> pageGrouponGoods(@RequestBody @Valid GrouponGoodsPageRequest
															grouponGoodsPageRequest);

	/**
	 * 分页查询拼团活动商品SKU信息API
	 *
	 * @author groupon
	 * @param grouponGoodsPageRequest 分页请求参数和筛选对象 {@link GrouponGoodsInfoSimplePageRequest}
	 * @return 分页查询拼团活动商品SPU信息 {@link GrouponGoodsInfoSimplePageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/sku-page")
	BaseResponse<GrouponGoodsInfoSimplePageResponse> pageGrouponGoodsInfo(@RequestBody @Valid GrouponGoodsInfoSimplePageRequest
																	grouponGoodsPageRequest);

	/**
	 * 根据活动ID、SKU编号查询拼团商品信息
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/get-by-groupon-activity-id-and-goods-info-id")
	BaseResponse<GrouponGoodsByGrouponActivityIdAndGoodsInfoIdResponse> getByGrouponActivityIdAndGoodsInfoId(@RequestBody @Valid GrouponGoodsByGrouponActivityIdAndGoodsInfoIdRequest request);

	/**
	 * 列表查询商品skuIds查询活动中的商品
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/list-by-goodsInfoIds")
	BaseResponse<GrouponGoodsInfoListResponse> listActivitying(@RequestBody @Valid GrouponGoodsByGoodsInfoIdAndTimeRequest request);

}

