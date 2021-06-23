package com.wanmi.sbc.goods.api.provider.goodsrestrictedsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.*;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSalePageResponse;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSaleListResponse;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSaleByIdResponse;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSalePurchaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>限售配置查询服务Provider</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsRestrictedSaleQueryProvider")
public interface GoodsRestrictedSaleQueryProvider {

	/**
	 * 分页查询限售配置API
	 *
	 * @author baijz
	 * @param goodsRestrictedSalePageReq 分页请求参数和筛选对象 {@link GoodsRestrictedSalePageRequest}
	 * @return 限售配置分页列表信息 {@link GoodsRestrictedSalePageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/page")
	BaseResponse<GoodsRestrictedSalePageResponse> page(@RequestBody @Valid GoodsRestrictedSalePageRequest goodsRestrictedSalePageReq);

	/**
	 * 列表查询限售配置API
	 *
	 * @author baijz
	 * @param goodsRestrictedSaleListReq 列表请求参数和筛选对象 {@link GoodsRestrictedSaleListRequest}
	 * @return 限售配置的列表信息 {@link GoodsRestrictedSaleListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/list")
	BaseResponse<GoodsRestrictedSaleListResponse> list(@RequestBody @Valid GoodsRestrictedSaleListRequest goodsRestrictedSaleListReq);

	/**
	 * 单个查询限售配置API
	 *
	 * @author baijz
	 * @param goodsRestrictedSaleByIdRequest 单个查询限售配置请求参数 {@link GoodsRestrictedSaleByIdRequest}
	 * @return 限售配置详情 {@link GoodsRestrictedSaleByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/get-by-id")
	BaseResponse<GoodsRestrictedSaleByIdResponse> getById(@RequestBody @Valid GoodsRestrictedSaleByIdRequest goodsRestrictedSaleByIdRequest);

	/**
	 * 校验商品限售的规则
	 * @param restrictedValidateRequest
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/validate-to-buy-imm")
	BaseResponse validateToByImm(@RequestBody @Valid GoodsRestrictedValidateRequest restrictedValidateRequest);

	/**
	 * 下单批量校验商品限售的规则
	 * @param restrictedBatchValidateRequest
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/validate-order-restricted")
	BaseResponse validateOrderRestricted(@RequestBody @Valid GoodsRestrictedBatchValidateRequest restrictedBatchValidateRequest);

	/**
	 * 购物车选择商品时限售的校验
	 * @param restrictedBatchValidateRequest
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/validate-purchase-restricted")
	BaseResponse<GoodsRestrictedSalePurchaseResponse> validatePurchaseRestricted(@RequestBody @Valid GoodsRestrictedBatchValidateRequest restrictedBatchValidateRequest);

	/**
	 * 下单批量校验商品限售的规则-简化
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/validate-order-restricted-simplify")
	BaseResponse validateOrderRestrictedSimplify(@RequestBody @Valid GoodsRestrictedBatchValidateOrderCommitRequest request);
}

