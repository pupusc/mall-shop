package com.wanmi.sbc.marketing.api.provider.marketingsuitssku;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.MarketingSuitsCountBySkuIdRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.MarketingSuitsSkuPageRequest;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketinSuitsCountBySkuIdResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuPageResponse;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.MarketingSuitsSkuListRequest;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuListResponse;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.MarketingSuitsSkuByIdRequest;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>组合活动关联商品sku表查询服务Provider</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingSuitsSkuQueryProvider")
public interface MarketingSuitsSkuQueryProvider {

	/**
	 * 分页查询组合活动关联商品sku表API
	 *
	 * @author zhk
	 * @param marketingSuitsSkuPageReq 分页请求参数和筛选对象 {@link MarketingSuitsSkuPageRequest}
	 * @return 组合活动关联商品sku表分页列表信息 {@link MarketingSuitsSkuPageResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuitssku/page")
	BaseResponse<MarketingSuitsSkuPageResponse> page(@RequestBody @Valid MarketingSuitsSkuPageRequest marketingSuitsSkuPageReq);

	/**
	 * 列表查询组合活动关联商品sku表API
	 *
	 * @author zhk
	 * @param marketingSuitsSkuListReq 列表请求参数和筛选对象 {@link MarketingSuitsSkuListRequest}
	 * @return 组合活动关联商品sku表的列表信息 {@link MarketingSuitsSkuListResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuitssku/list")
	BaseResponse<MarketingSuitsSkuListResponse> list(@RequestBody @Valid MarketingSuitsSkuListRequest marketingSuitsSkuListReq);

	/**
	 * 单个查询组合活动关联商品sku表API
	 *
	 * @author zhk
	 * @param marketingSuitsSkuByIdRequest 单个查询组合活动关联商品sku表请求参数 {@link MarketingSuitsSkuByIdRequest}
	 * @return 组合活动关联商品sku表详情 {@link MarketingSuitsSkuByIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuitssku/get-by-id")
	BaseResponse<MarketingSuitsSkuByIdResponse> getById(@RequestBody @Valid MarketingSuitsSkuByIdRequest marketingSuitsSkuByIdRequest);

	/**
	 *  统计组合类型-商品数量
	 * @param
	 * @Return Integer
	 */
	@PostMapping("/marketing/${application.marketing.version}/getMarketingCountBySkuId")
	BaseResponse<MarketinSuitsCountBySkuIdResponse> getMarketingCountBySkuId(@RequestBody @Valid MarketingSuitsCountBySkuIdRequest marketingSuitsCountBySkuIdRequest);


}

