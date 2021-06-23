package com.wanmi.sbc.goods.api.provider.goodsevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodsevaluate.*;
import com.wanmi.sbc.goods.api.response.goodsevaluate.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品评价查询服务Provider</p>
 * @author liutao
 * @date 2019-02-25 15:17:42
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsEvaluateQueryProvider")
public interface GoodsEvaluateQueryProvider {

	/**
	 * 分页查询商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluatePageReq 分页请求参数和筛选对象 {@link GoodsEvaluatePageRequest}
	 * @return 商品评价分页列表信息 {@link GoodsEvaluatePageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/page")
	BaseResponse<GoodsEvaluatePageResponse> page(@RequestBody @Valid GoodsEvaluatePageRequest goodsEvaluatePageReq);

	/**
	 * 列表查询商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluateListReq 列表请求参数和筛选对象 {@link GoodsEvaluateListRequest}
	 * @return 商品评价的列表信息 {@link GoodsEvaluateListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/list")
	BaseResponse<GoodsEvaluateListResponse> list(@RequestBody @Valid GoodsEvaluateListRequest goodsEvaluateListReq);

	/**
	 * 单个查询商品评价API
	 *
	 * @author liutao
	 * @param goodsEvaluateByIdRequest 单个查询商品评价请求参数 {@link GoodsEvaluateByIdRequest}
	 * @return 商品评价详情 {@link GoodsEvaluateByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/get-by-id")
	BaseResponse<GoodsEvaluateByIdResponse> getById(@RequestBody @Valid GoodsEvaluateByIdRequest goodsEvaluateByIdRequest);

	/**
	 * 获取已评价商品数量
	 * @param queryReq
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/get-goods-evaluate-num")
	BaseResponse<Long> getGoodsEvaluateNum(@RequestBody GoodsEvaluateQueryRequest queryReq);

	/**
	 * @Description: 该spu商品评价总数、晒单、好评率
	 * @param request {@link GoodsEvaluateCountRequset}
	 * @Author: Bob
	 * @Date: 2019-04-04 16:17
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/get-goods-evaluate-sum")
	BaseResponse<GoodsEvaluateCountResponse> getGoodsEvaluateSum(@RequestBody @Valid GoodsEvaluateCountRequset request);

	/**
	 * @Description: 查询最新评价<排除系统评价>
	 * @param request {@link GoodsEvaluatePageRequest}
	 * @Author: Bob
	 * @Date: 2019-05-29 17:49
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluate/get-goods-top-data")
	BaseResponse<GoodsEvaluateListResponse> getGoodsEvaluateTopData(@RequestBody @Valid GoodsEvaluatePageRequest request);

	/**
	 * @Description: 根据skuid查询该spu商品评价总数、晒单、好评率
	 * @param request {@link GoodsEvaluateCountRequset}
	 * @Author: yangzhen
	 * @Date: 2020-09-02 16:17
	 */
	@PostMapping("/goods/${application.goods.version}/goodsevaluateBySkuId/get-goods-evaluate-sum-by-skuid")
	BaseResponse<GoodsEvaluateCountResponse> getGoodsEvaluateSumByskuId(@RequestBody @Valid GoodsEvaluateCountBySkuIdRequset request);

}

