package com.wanmi.sbc.customer.api.provider.storeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluatePageRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluatePageResponse;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateListRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateListResponse;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateByIdRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>店铺评价查询服务Provider</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreEvaluateQueryProvider")
public interface StoreEvaluateQueryProvider {

	/**
	 * 分页查询店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluatePageReq 分页请求参数和筛选对象 {@link StoreEvaluatePageRequest}
	 * @return 店铺评价分页列表信息 {@link StoreEvaluatePageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluate/page")
	BaseResponse<StoreEvaluatePageResponse> page(@RequestBody @Valid StoreEvaluatePageRequest storeEvaluatePageReq);

	/**
	 * 列表查询店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateListReq 列表请求参数和筛选对象 {@link StoreEvaluateListRequest}
	 * @return 店铺评价的列表信息 {@link StoreEvaluateListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluate/list")
	BaseResponse<StoreEvaluateListResponse> list(@RequestBody @Valid StoreEvaluateListRequest storeEvaluateListReq);

	/**
	 * 单个查询店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateByIdRequest 单个查询店铺评价请求参数 {@link StoreEvaluateByIdRequest}
	 * @return 店铺评价详情 {@link StoreEvaluateByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluate/get-by-id")
	BaseResponse<StoreEvaluateByIdResponse> getById(@RequestBody @Valid StoreEvaluateByIdRequest storeEvaluateByIdRequest);

}

