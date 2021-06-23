package com.wanmi.sbc.goods.api.provider.goodstobeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.*;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateAddResponse;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateByIdResponse;
import com.wanmi.sbc.goods.api.response.goodstobeevaluate.GoodsTobeEvaluateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>订单商品待评价保存服务Provider</p>
 * @author lzw
 * @date 2019-03-20 11:21:41
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsTobeEvaluateSaveProvider")
public interface GoodsTobeEvaluateSaveProvider {

	/**
	 * 新增订单商品待评价API
	 *
	 * @author lzw
	 * @param goodsTobeEvaluateAddRequest 订单商品待评价新增参数结构 {@link GoodsTobeEvaluateAddRequest}
	 * @return 新增的订单商品待评价信息 {@link GoodsTobeEvaluateAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/add")
	BaseResponse<GoodsTobeEvaluateAddResponse> add(@RequestBody @Valid GoodsTobeEvaluateAddRequest goodsTobeEvaluateAddRequest);

	/**
	 * 修改订单商品待评价API
	 *
	 * @author lzw
	 * @param goodsTobeEvaluateModifyRequest 订单商品待评价修改参数结构 {@link GoodsTobeEvaluateModifyRequest}
	 * @return 修改的订单商品待评价信息 {@link GoodsTobeEvaluateModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/modify")
	BaseResponse<GoodsTobeEvaluateModifyResponse> modify(@RequestBody @Valid GoodsTobeEvaluateModifyRequest goodsTobeEvaluateModifyRequest);

	/**
	 * 单个删除订单商品待评价API
	 *
	 * @author lzw
	 * @param goodsTobeEvaluateDelByIdRequest 单个删除参数结构 {@link GoodsTobeEvaluateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GoodsTobeEvaluateDelByIdRequest goodsTobeEvaluateDelByIdRequest);

	/**
	 * 批量删除订单商品待评价API
	 *
	 * @author lzw
	 * @param goodsTobeEvaluateDelByIdListRequest 批量删除参数结构 {@link GoodsTobeEvaluateDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid GoodsTobeEvaluateDelByIdListRequest goodsTobeEvaluateDelByIdListRequest);

	/**
	 * @Description: 订单ID和skuID删除
	 * @Author: Bob
	 * @Date: 2019-04-12 16:29
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/delete-by-order-sku")
	BaseResponse<Integer> deleteByOrderAndSku(@RequestBody @Valid GoodsTobeEvaluateQueryRequest goodsTobeEvaluateQueryRequest);

	/**
	 * @Description: 动态条件查询
	 * @Author: Bob
	 * @Date: 2019-04-12 17:19
	 */
	@PostMapping("/goods/${application.goods.version}/goodstobeevaluate/query")
	BaseResponse<GoodsTobeEvaluateByIdResponse> query(@RequestBody @Valid GoodsTobeEvaluateQueryRequest goodsTobeEvaluateQueryRequest);



}

