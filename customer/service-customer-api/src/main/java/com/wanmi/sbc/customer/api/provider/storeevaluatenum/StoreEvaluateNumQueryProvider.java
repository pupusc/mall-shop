package com.wanmi.sbc.customer.api.provider.storeevaluatenum;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumPageRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumPageResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumListRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumListResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumByIdRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumByIdResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>店铺统计评分等级人数统计查询服务Provider</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreEvaluateNumQueryProvider")
public interface StoreEvaluateNumQueryProvider {

	/**
	 * 分页查询店铺统计评分等级人数统计API
	 *
	 * @author liutao
	 * @param storeEvaluateNumPageReq 分页请求参数和筛选对象 {@link StoreEvaluateNumPageRequest}
	 * @return 店铺统计评分等级人数统计分页列表信息 {@link StoreEvaluateNumPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/page")
	BaseResponse<StoreEvaluateNumPageResponse> page(@RequestBody @Valid StoreEvaluateNumPageRequest storeEvaluateNumPageReq);

	/**
	 * 列表查询店铺统计评分等级人数统计API
	 *
	 * @author liutao
	 * @param storeEvaluateNumListReq 列表请求参数和筛选对象 {@link StoreEvaluateNumListRequest}
	 * @return 店铺统计评分等级人数统计的列表信息 {@link StoreEvaluateNumListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/list")
	BaseResponse<StoreEvaluateNumListResponse> list(@RequestBody @Valid StoreEvaluateNumListRequest storeEvaluateNumListReq);

	/**
	 * 单个查询店铺统计评分等级人数统计API
	 *
	 * @author liutao
	 * @param storeEvaluateNumByIdRequest 单个查询店铺统计评分等级人数统计请求参数 {@link StoreEvaluateNumByIdRequest}
	 * @return 店铺统计评分等级人数统计详情 {@link StoreEvaluateNumByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/get-by-id")
	BaseResponse<StoreEvaluateNumByIdResponse> getById(@RequestBody @Valid StoreEvaluateNumByIdRequest storeEvaluateNumByIdRequest);

	/**
	 * 根据店铺id和统计周期查询
	 * @param listRequest
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/list-by-storeId-scoreCycle")
	BaseResponse<StoreEvaluateNumResponse> listByStoreIdAndScoreCycle(@RequestBody @Valid
																		  StoreEvaluateNumListRequest listRequest);
}

