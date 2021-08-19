package com.wanmi.sbc.customer.api.provider.storeevaluatesum;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumPageRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumQueryRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumPageResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumListRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumListResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumByIdRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>店铺评价查询服务Provider</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreEvaluateSumQueryProvider")
public interface StoreEvaluateSumQueryProvider {

	/**
	 * 分页查询店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumPageReq 分页请求参数和筛选对象 {@link StoreEvaluateSumPageRequest}
	 * @return 店铺评价分页列表信息 {@link StoreEvaluateSumPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/page")
	BaseResponse<StoreEvaluateSumPageResponse> page(@RequestBody @Valid StoreEvaluateSumPageRequest storeEvaluateSumPageReq);

	/**
	 * 列表查询店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumListReq 列表请求参数和筛选对象 {@link StoreEvaluateSumListRequest}
	 * @return 店铺评价的列表信息 {@link StoreEvaluateSumListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/list")
	BaseResponse<StoreEvaluateSumListResponse> list(@RequestBody @Valid StoreEvaluateSumListRequest storeEvaluateSumListReq);

	/**
	 * 单个查询店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumByIdRequest 单个查询店铺评价请求参数 {@link StoreEvaluateSumByIdRequest}
	 * @return 店铺评价详情 {@link StoreEvaluateSumByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/get-by-id")
	BaseResponse<StoreEvaluateSumByIdResponse> getById(@RequestBody @Valid StoreEvaluateSumByIdRequest storeEvaluateSumByIdRequest);

	/**
	 * 根据店铺id查询店铺评价信息 30 90 180的
	 *
	 * @author liutao
	 * @param storeEvaluateSumQueryRequest 单个查询店铺评价请求参数 {@link StoreEvaluateSumByIdRequest}
	 * @return 店铺评价详情 {@link StoreEvaluateSumByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/get-by-store-id")
	BaseResponse<StoreEvaluateSumByIdResponse> getByStoreId(@RequestBody @Valid StoreEvaluateSumQueryRequest storeEvaluateSumQueryRequest);



}

