package com.wanmi.sbc.goods.api.provider.goodstobeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateByIdRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateListRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluatePageRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateByIdResponse;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateListResponse;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluatePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>订单商品待评价查询服务Provider</p>
 * @author lzw
 * @date 2019-03-20 11:21:41
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsTobeEvaluateQueryProvider")
public interface GoodsTobeEvaluateQueryProvider {

	/**
	 * 分页查询订单商品待评价API
	 *
	 * @author lzw
	 * @param goodsTobeEvaluatePageReq 分页请求参数和筛选对象 {@link GoodsTobeEvaluatePageRequest}
	 * @return 订单商品待评价分页列表信息 {@link GoodsTobeEvaluatePageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/page")
	BaseResponse<GoodsTobeEvaluatePageResponse> page(@RequestBody @Valid GoodsTobeEvaluatePageRequest goodsTobeEvaluatePageReq);

	/**
	 * 列表查询订单商品待评价API
	 *
	 * @author lzw
	 * @param goodsTobeEvaluateListReq 列表请求参数和筛选对象 {@link GoodsTobeEvaluateListRequest}
	 * @return 订单商品待评价的列表信息 {@link GoodsTobeEvaluateListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/list")
	BaseResponse<GoodsTobeEvaluateListResponse> list(@RequestBody @Valid GoodsTobeEvaluateListRequest goodsTobeEvaluateListReq);

	/**
	 * 单个查询订单商品待评价API
	 *
	 * @author lzw
	 * @param goodsTobeEvaluateByIdRequest 单个查询订单商品待评价请求参数 {@link GoodsTobeEvaluateByIdRequest}
	 * @return 订单商品待评价详情 {@link GoodsTobeEvaluateByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/get-by-id")
	BaseResponse<GoodsTobeEvaluateByIdResponse> getById(@RequestBody @Valid GoodsTobeEvaluateByIdRequest goodsTobeEvaluateByIdRequest);

	/**
	 * 获取待评价数量
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/get-goods-tobe-evaluate-num")
	BaseResponse<Long> getGoodsTobeEvaluateNum(@RequestBody GoodsTobeEvaluateQueryRequest queryReq);

	/**
	 * @Author lvzhenwei
	 * @Description 待评价商品自动评价
	 * @Date 9:42 2019/4/25
	 * @Param []
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/auto-goods-evaluate")
	BaseResponse autoGoodsEvaluate();

}

