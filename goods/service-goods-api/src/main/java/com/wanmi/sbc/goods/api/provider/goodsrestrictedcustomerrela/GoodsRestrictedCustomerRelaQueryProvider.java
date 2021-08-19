package com.wanmi.sbc.goods.api.provider.goodsrestrictedcustomerrela;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaPageRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaPageResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaListRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaListResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaByIdRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>限售配置会员关系表查询服务Provider</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@FeignClient(value = "${application.goods.name}" , contextId = "GoodsRestrictedCustomerRelaQueryProvider")
public interface GoodsRestrictedCustomerRelaQueryProvider {

	/**
	 * 分页查询限售配置会员关系表API
	 *
	 * @author baijz
	 * @param goodsRestrictedCustomerRelaPageReq 分页请求参数和筛选对象 {@link GoodsRestrictedCustomerRelaPageRequest}
	 * @return 限售配置会员关系表分页列表信息 {@link GoodsRestrictedCustomerRelaPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedcustomerrela/page")
	BaseResponse<GoodsRestrictedCustomerRelaPageResponse> page(@RequestBody @Valid GoodsRestrictedCustomerRelaPageRequest goodsRestrictedCustomerRelaPageReq);

	/**
	 * 列表查询限售配置会员关系表API
	 *
	 * @author baijz
	 * @param goodsRestrictedCustomerRelaListReq 列表请求参数和筛选对象 {@link GoodsRestrictedCustomerRelaListRequest}
	 * @return 限售配置会员关系表的列表信息 {@link GoodsRestrictedCustomerRelaListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedcustomerrela/list")
	BaseResponse<GoodsRestrictedCustomerRelaListResponse> list(@RequestBody @Valid GoodsRestrictedCustomerRelaListRequest goodsRestrictedCustomerRelaListReq);

	/**
	 * 单个查询限售配置会员关系表API
	 *
	 * @author baijz
	 * @param goodsRestrictedCustomerRelaByIdRequest 单个查询限售配置会员关系表请求参数 {@link GoodsRestrictedCustomerRelaByIdRequest}
	 * @return 限售配置会员关系表详情 {@link GoodsRestrictedCustomerRelaByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedcustomerrela/get-by-id")
	BaseResponse<GoodsRestrictedCustomerRelaByIdResponse> getById(@RequestBody @Valid GoodsRestrictedCustomerRelaByIdRequest goodsRestrictedCustomerRelaByIdRequest);

}

