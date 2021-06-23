package com.wanmi.sbc.marketing.api.provider.marketingsuits;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.market.MarketingSuitsBySkuIdRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.*;
import com.wanmi.sbc.marketing.api.response.market.MarketingMoreGoodsInfoResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingMoreSuitsGoodInfoIdResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuits.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>组合商品主表查询服务Provider</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingSuitsQueryProvider")
public interface MarketingSuitsQueryProvider {

	/**
	 * 分页查询组合商品主表API
	 *
	 * @author zhk
	 * @param marketingSuitsPageReq 分页请求参数和筛选对象 {@link MarketingSuitsPageRequest}
	 * @return 组合商品主表分页列表信息 {@link MarketingSuitsPageResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/page")
	BaseResponse<MarketingSuitsPageResponse> page(@RequestBody @Valid MarketingSuitsPageRequest marketingSuitsPageReq);

	/**
	 * 列表查询组合商品主表API
	 *
	 * @author zhk
	 * @param marketingSuitsListReq 列表请求参数和筛选对象 {@link MarketingSuitsListRequest}
	 * @return 组合商品主表的列表信息 {@link MarketingSuitsListResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/list")
	BaseResponse<MarketingSuitsListResponse> list(@RequestBody @Valid MarketingSuitsListRequest marketingSuitsListReq);

	/**
	 * 单个查询组合商品主表API
	 *
	 * @author zhk
	 * @param marketingSuitsByIdRequest 单个查询组合商品主表请求参数 {@link MarketingSuitsByIdRequest}
	 * @return 组合商品主表详情 {@link MarketingSuitsByIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/get-by-id")
	BaseResponse<MarketingSuitsByIdResponse> getById(@RequestBody @Valid MarketingSuitsByIdRequest marketingSuitsByIdRequest);




	/**
	 * 根据营销活动id查询组合购活动信息
	 *
	 * @author zhk
	 * @param marketingSuitsByMarketingIdRequest 根据营销活动id查询组合购活动信息 {@link MarketingSuitsByMarketingIdRequest}
	 * @return 组合商品主表详情 {@link MarketingSuitsByMarketingIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/get-by-marketing-id")
	BaseResponse<MarketingSuitsByMarketingIdResponse> getByMarketingId(@RequestBody @Valid MarketingSuitsByMarketingIdRequest marketingSuitsByMarketingIdRequest);



    /**
     * 根据营销活动id查询组合购活动信息
     *
     * @author zhk
     * @param marketingSuitsValidRequest 根据营销活动id查询组合购活动信息 {@link MarketingSuitsByMarketingIdRequest}
     * @return 组合商品主表详情 {@link MarketingSuitsByMarketingIdResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/marketingsuits/suit-order-valid-before-commit")
    BaseResponse<MarketingSuitsValidResponse> validSuitOrderBeforeCommit(@RequestBody @Valid MarketingSuitsValidRequest marketingSuitsValidRequest);

	/**
	 * 根据营销活动id查询组合购活动信息
	 *
	 * @author zhk
	 * @param marketingSuitsByMarketingIdRequest 根据营销活动id查询组合购活动信息 {@link MarketingSuitsByMarketingIdRequest}
	 * @return 组合商品主表详情 {@link MarketingSuitsByMarketingIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/get-by-marketing-id-for-supplier")
	BaseResponse<MarketingSuitsByMarketingIdResponse> getByMarketingIdForSupplier(@RequestBody @Valid MarketingSuitsByMarketingIdRequest marketingSuitsByMarketingIdRequest);

	/**
	 * 根据skuId查询活动信息
	 *
	 * @author
	 * @param request 根据营销活动id查询组合购活动信息 {@link MarketingSuitsBySkuIdRequest}
	 * @return 组合商品主表详情 {@link MarketingMoreGoodsInfoResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/get-marketing-by-sku-id")
	BaseResponse<MarketingMoreSuitsGoodInfoIdResponse> getMarketingBySuitsSkuId(@RequestBody @Valid MarketingSuitsBySkuIdRequest request);

}

